package com.example.ticketservice.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TicketGenerator {
    static int[][] ticket;
    static Set<Integer> set;

    public static int[][] generate() {
        set = new HashSet<>();
        ticket = new int[3][9];

        for(int i=0;i<9;i++){
            int[] arr = genNum(i*10,(i+1)*10);
            ticket[0][i] = arr[0];
            ticket[1][i] = arr[1];
            ticket[2][i] = arr[2];
        }

        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++){
                if(i==0){
                    int index = generateRandom(0,9);
                    ticket[i][index] = 0;
                }
                else{
                    int index = generateRandom(0,9);
                    while(ticket[i-1][index]==0){
                        index = generateRandom(0,9);
                    }
                    ticket[i][index] = 0;
                }
            }
            set.clear();
        }

        return ticket;
    }

    public static int[] genNum(int min, int max){
        if(min==0) min = 1;
        Set<Integer> set = new HashSet<>();
        int arr[] = new int[3];
        int i=0;
        while(i<3){
            int random = ThreadLocalRandom.current().nextInt(min, max);
            if(!set.contains(random)){
                arr[i++] =  random;
                set.add(random);
            }
        }
        Arrays.sort(arr);
        return arr;
    }

    public static int generateRandom(int min, int max){
        int random = ThreadLocalRandom.current().nextInt(min, max);

        while(set.contains(random)){
            random = ThreadLocalRandom.current().nextInt(min, max);
        }
        set.add(random);
        return random;
    }
}
