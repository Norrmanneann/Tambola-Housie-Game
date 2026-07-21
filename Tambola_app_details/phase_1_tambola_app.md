---

## 18) MVP Quality Phase Plan (Detailed)

For this project, we will implement **Phase 1 now** to ensure the app is stable and demo-ready.  
Phase 2 improvements are intentionally deferred until the full end-to-end MVP works.

---

### Phase 1 (Must Implement Now)

## 18.1 Server-Authoritative Timer

**What it means**  
The backend controls game timing (countdown + draw interval).  
Frontend only displays whatever backend sends.

**Why it is important**  
If clients control time, every player can see different timing due to network/device lag.  
That causes unfair gameplay and inconsistent claims.

**Implementation approach**
- In `Game Service`, start scheduler on game start.
- Use configured interval (3/5/7 sec).
- On each tick:
  1. pick next number
  2. persist it
  3. broadcast `NUMBER_CALLED`
- Optional: broadcast countdown events before each draw.

**Minimum acceptance criteria**
- Draws happen even if one client disconnects.
- Two different clients receive same sequence/order from server.
- Restarting client does not affect game timer.

---

## 18.2 Atomic Claim Validation + Unique Winner Constraint

**What it means**  
Claim verification and winner insertion must happen in one transaction so concurrent claims cannot create duplicate winners.

**Why it is important**  
Two players may claim same pattern at nearly same time.  
Without transaction + DB constraint, both can be marked winner.

**Implementation approach**
- Wrap claim validation flow in DB transaction.
- Check if winner already exists for `(room_code, pattern_type)`.
- If no winner:
  - validate claim against called numbers
  - insert winner
  - mark claim accepted
- If already won:
  - mark claim rejected with reason `ALREADY_CLAIMED`

**DB rule (required)**
- Add unique constraint:  
  - `UNIQUE(room_code, pattern_type)` for single-winner patterns.

**Minimum acceptance criteria**
- Parallel claim requests for same pattern result in only one accepted winner.
- Rejected claim has clear reason.

---

## 18.3 Join Restriction After Game Start

**What it means**  
New players cannot join once room/game status becomes `RUNNING`.

**Why it is important**  
Late joins break fairness and ticket lifecycle consistency.

**Implementation approach**
- In `Room Service join API`, check room status before adding player.
- If `RUNNING` or `ENDED`, reject with business error (e.g., `ROOM_NOT_JOINABLE`).

**Minimum acceptance criteria**
- Join works only in `WAITING`.
- Join always rejected in `RUNNING`/`ENDED`.

---

## 18.4 Input Validation (Minimum Set)

**What it means**  
Validate essential fields at API boundary and business layer.

**Why it is important**  
Prevents invalid state and avoids debugging random failures later.

**Implementation approach**

### Room validations
- `maxPlayers` allowed range (example: 2–10)
- `callingIntervalSec` allowed values (3, 5, 7)
- valid winning pattern list (non-empty)

### Join validations
- room exists
- not full
- status is `WAITING`
- player exists

### Claim validations
- pattern is valid enum
- room exists and game in progress
- player belongs to room
- ticket belongs to that player and room
- numbers required by pattern are present in called numbers

**Minimum acceptance criteria**
- Invalid requests return 4xx with clear message.
- No null/invalid pattern reaches core claim logic.

---

## 18.5 Audit Persistence (Draws + Claims)

**What it means**  
Store traceable records for critical actions:
1. every drawn number
2. every claim attempt and result

**Why it is important**  
Useful for dispute resolution, debugging, and interview/demo proof.

**Implementation approach**

### Draw audit table/data
- roomCode
- drawIndex
- numberCalled
- calledAt (timestamp)

### Claim audit table/data
- roomCode
- playerId
- ticketId
- pattern
- status (`ACCEPTED`/`REJECTED`)
- reason
- createdAt

**Minimum acceptance criteria**
- You can reconstruct game sequence from DB.
- You can explain why each claim was accepted/rejected.

---

## 18.6 Standard Error Response (Basic)

**What it means**  
All services return errors in same JSON shape.

**Why it is important**  
Frontend integration becomes easy and predictable.

**Standard error shape**
```json
{
  "timestamp": "2026-07-20T12:00:00Z",
  "service": "room-service",
  "code": "ROOM_NOT_JOINABLE",
  "message": "Room is already running",
  "path": "/api/v1/rooms/join"
}
```

**Implementation approach**
- Use global exception handler (`@RestControllerAdvice`) in each service.
- Map business exceptions to meaningful `code` values.
- Keep message human-readable.

**Minimum acceptance criteria**
- Validation errors and business errors both follow same format.
- Frontend can show message directly.

---

### Phase 2 (Deferred, Not Required for Current MVP)

These are quality upgrades for later:
- Idempotency keys
- Ticket lock timeout auto-release
- Host failover/promotion
- Claim rate limiting
- Correlation ID tracing across services
- Deterministic seeded shuffle test mode
- Advanced WebSocket reconnect snapshot system

---

## 18.7 Definition of Done for Phase 1

Phase 1 is complete only when all are true:

- [ ] Timer is fully backend-driven
- [ ] Concurrent claims cannot create duplicate winners
- [ ] Join blocked after game starts
- [ ] Input validations implemented for room/join/claim
- [ ] Draw and claim audits are persisted
- [ ] Unified error response used in all services

---