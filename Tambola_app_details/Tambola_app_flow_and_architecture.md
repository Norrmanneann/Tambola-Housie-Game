# Tambola Backend – Final Architecture & Detailed Flow (MVP)

## 1) Goal of This Document
This file is the **single source of truth** for project structure and implementation flow.  
Whenever confused, follow this document step-by-step.

---

## 2) Final MVP Scope

- Microservices with Spring Boot
- Eureka for service discovery
- OpenFeign for inter-service communication
- PostgreSQL (database per service)
- Docker / Docker Compose
- WebSocket for real-time updates (**inside Game Service**)
- No Auth (JWT) for now
- No Redis for now
- No Kafka/RabbitMQ for now

---

## 3) Final Architecture

```text
                    React + TypeScript / Postman
                              │
                      REST + WebSocket(STOMP)
                              │
                    Spring Cloud Gateway (optional first)
                              │
                        Eureka Server
                 (Service Discovery Registry)
                              │
      ┌──────────────┬──────────────┬──────────────┬──────────────┐
      │              │              │              │
      ▼              ▼              ▼              ▼
 Player Service   Room Service   Ticket Service   Game Service
 (player profile) (room mgmt)    (ticket mgmt)    (game engine +
                                                draw + claim + ws broadcast)
      │              │              │              │
      └──────────────┴───────┬──────┴──────────────┘
                             │
                    OpenFeign REST calls
                             │
       ┌──────────────┬──────────────┬──────────────┬──────────────┐
       ▼              ▼              ▼              ▼
   Player DB       Room DB       Ticket DB       Game DB
  PostgreSQL      PostgreSQL     PostgreSQL      PostgreSQL
```

---

## 4) Why WebSocket Inside Game Service (for MVP)

Two options:
1. Keep WebSocket inside Game Service
2. Separate WebSocket Service

For MVP we choose **Option 1** because:
- less complexity
- faster delivery
- fewer moving parts
- still valid architecture

Future upgrade:
- extract WebSocket broadcast into dedicated service when scale increases.

---

## 5) Service Responsibilities

## Player Service
- Create/find player
- Stores:
  - playerId (UUID)
  - playerName
  - createdAt

### Room Service
- Create room
- Join room
- Room validations:
  - room exists
  - not full
  - not started
- Stores:
  - roomCode
  - hostPlayerId
  - hostSecret
  - maxPlayers
  - status (WAITING/RUNNING/ENDED)

### Ticket Service
- Generate Tambola tickets (3 options)
- Allow player to select one ticket
- Lock selected ticket
- Stores:
  - ticketId
  - roomCode
  - playerId
  - ticketMatrix / ticketNumbers
  - selected (true/false)
  - lockedAt

### Game Service
- Start game
- Shuffle and draw numbers (1–90)
- Maintain running game state
- Validate claims (by checking ticket + called numbers)
- Persist winners and claim results
- Broadcast all realtime events via WebSocket

---

## 6) Inter-Service Communication (OpenFeign)

- Game Service → Room Service
  - verify host before start/end
  - verify room status
- Game Service → Ticket Service
  - fetch ticket for claim validation
- Game Service → Room Service
  - verify player belongs to room (claim stage)

---

## 7) Game State Model (In-Memory + DB Snapshot)

For each active room, Game Service maintains:

- roomCode
- shuffledDeck (1..90)
- currentIndex
- calledNumbers
- gameStatus
- winnersByPattern
- configuredPatterns
- scheduler/timer metadata

In-memory for speed; important state also persisted to Game DB.

---

## 8) WebSocket Topics & Events

### Topic format
`/topic/room/{roomCode}`

### Events
- PLAYER_JOINED
- GAME_STARTED
- COUNTDOWN
- NUMBER_CALLED
- CLAIM_ACCEPTED
- CLAIM_REJECTED
- WINNER
- GAME_ENDED

Example payload:
```json
{
  "event": "NUMBER_CALLED",
  "roomCode": "ABC123",
  "number": 54,
  "calledNumbersCount": 17,
  "timestamp": "2026-07-20T10:15:30Z"
}
```

---

## 9) Detailed End-to-End Flow

## Step 1: Player enters name
- Frontend asks player name
- Call Player Service: create player
- Receive `playerId`
- Frontend stores `playerId`, `playerName` locally

## Step 2: Host creates room
- Host provides room config:
  - maxPlayers
  - callingIntervalSec
  - winningPatterns
- Call Room Service create room
- Room Service generates:
  - `roomCode`
  - `hostSecret`
- Save room with `WAITING` state
- Return room details

## Step 3: Host ticket selection
- Host calls Ticket Service for ticket options
- Ticket Service returns 3 generated tickets
- Host selects one
- Ticket is locked for host

## Step 4: Players join room
- Player enters `roomCode`
- Room Service validates join constraints
- On success player is added
- Ticket Service generates 3 options for that player
- Player selects one; ticket locked

## Step 5: Waiting lobby updates
- Clients connect WebSocket and subscribe:
  - `/topic/room/{roomCode}`
- On each join, broadcast PLAYER_JOINED
- UI updates participant list instantly

## Step 6: Host starts game
- Host calls Game Service start endpoint with `roomCode + hostSecret`
- Game Service verifies host/room via Room Service
- Creates shuffled deck 1..90
- Sets state `RUNNING`
- Broadcasts GAME_STARTED and optional countdown

## Step 7: Number calling loop
- Scheduler runs every configured interval (e.g., 5 sec)
- Picks next number from deck
- Appends to calledNumbers
- Persists progress
- Broadcasts NUMBER_CALLED

## Step 8: Claim submission
- Player submits claim with:
  - roomCode
  - playerId
  - ticketId
  - pattern
- Game Service validates:
  - player belongs to room
  - ticket belongs to player and room
  - pattern numbers are already called
  - winner for pattern not already finalized (if single winner)
- Save claim result
- Broadcast ACCEPTED/REJECTED
- If accepted, broadcast WINNER

## Step 9: Continue until end condition
- Drawing continues
- Claims continue
- End when:
  - full house claimed OR
  - deck exhausted OR
  - host manually ends

## Step 10: End game
- Game state marked `ENDED`
- Final winners persisted
- Broadcast GAME_ENDED
- Lobby can reset for new round

---

## 10) Suggested Claim Rules (MVP)

Patterns:
- EARLY_FIVE
- TOP_LINE
- MIDDLE_LINE
- BOTTOM_LINE
- FULL_HOUSE

Validation:
- Ticket ownership valid
- Pattern not already awarded (for single-winner patterns)
- All required numbers for pattern are in calledNumbers

---

## 11) Data Integrity Rules (Important)

In Game DB / Winners table:
- unique constraint on `(room_code, pattern_type)` for single-winner patterns

This prevents duplicate winners during simultaneous claims.

Also:
- Use transaction boundary around:
  - check existing winner
  - insert winner
  - mark claim accepted

---

## 12) Room & Game State Machines

### Room
`WAITING -> RUNNING -> ENDED`

### Game
`NOT_STARTED -> IN_PROGRESS -> COMPLETED`

---

## 13) MVP API Checklist (High-Level)

### Player Service
- create player
- get player

### Room Service
- create room
- join room
- get room details/players
- verify host (internal)

### Ticket Service
- generate ticket options
- select/lock ticket
- get player ticket (internal)

### Game Service
- start game
- draw number (manual fallback)
- claim prize
- get called numbers
- get winners
- end game

---

## 14) Build Order (Do This Sequence)

1. Player Service
2. Room Service
3. Ticket Service
4. Game Service basic start/draw
5. Game claim validation
6. WebSocket broadcast in Game Service
7. Eureka + OpenFeign wiring
8. Docker Compose
9. Gateway polish
10. Optional enhancements

---

## 15) Future Enhancements (Post-MVP)

- JWT Auth + role-based access
- Redis for fast game state/cache/distributed lock
- Event bus (RabbitMQ/Kafka)
- Separate WebSocket Service
- Observability (Zipkin/Prometheus/Grafana)
- Deployment to AWS

---

## 16) Daily Use (When You Forget Flow)

Before coding:
1. Read Sections 3, 5, 9 (2 minutes)
2. Pick one step only
3. Implement + test with Postman
4. Update this file if flow changed

This prevents confusion and keeps project consistent.