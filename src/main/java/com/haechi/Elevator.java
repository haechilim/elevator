package com.haechi;

import java.util.Scanner;

public class Elevator {
    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 10;

    private static final int STOP = 0;
    private static final int NONE = 0;
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int BOTH = 2;

    private static int floor = 1;
    private static int direction = STOP;
    private static int[] stops = new int[MAX_FLOOR];

    public static void main(String[] args) {
        initStops();

        while(true) {
            printCurrentFloor();
            pushOutdoorButton();

            if(direction == STOP) {
                if(shouldOpenDoor()) {
                    turnOffUpButton();
                    turnOffDownButton();
                    System.out.println("문열림");
                    pushIndoorButton();
                }

                decideDirection();
            }
            else {
                if(direction == UP) {
                    if(shouldOpenDoor()) {
                        turnOffUpButton();
                        System.out.println("문열림");
                        pushIndoorButton();
                    }

                    if(shouldStop()) direction = STOP;
                    else if(!shouldGoUp()) {
                        direction = DOWN;
                        continue;
                    }
                }
                else if(direction == DOWN) {
                    if(shouldOpenDoor()) {
                        turnOffDownButton();
                        System.out.println("문열림");
                        pushIndoorButton();
                    }

                    if(shouldStop()) direction = STOP;
                    else if(!shouldGoDown()) {
                        direction = UP;
                        continue;
                    }
                }
            }

            floor += direction;

            if(floor > MAX_FLOOR) {
                floor = MAX_FLOOR;
                direction = STOP;
            }
            else if(floor < MIN_FLOOR) {
                floor = MIN_FLOOR;
                direction = STOP;
            }
        }
    }

    private static void printCurrentFloor() {
        System.out.print("\n================================================ " + floor + "층");

        if(direction == UP) System.out.print("↑");
        else if(direction == DOWN) System.out.print("↓");
        System.out.println();
    }

    private static void pushOutdoorButton() {
        String keyInput;
        Scanner scanner = new Scanner(System.in);
        int push;

        while(true) {
            System.out.print("외부버튼(+/-층수) 계속하기(엔터)");

            keyInput = scanner.nextLine();

            if(!keyInput.isEmpty()) {
                push = Integer.parseInt(keyInput);

                int index = Math.abs(push) - 1;

                if(index >= 0 && index < stops.length) {
                    if(push > 0) stops[index] = stops[index] == DOWN ? BOTH : UP;
                    else if(push < 0) stops[index] = stops[index] == UP ? BOTH : DOWN;
                    break;
                }
                else System.out.println(">> 올바른 층수를 입력해주세요.");
            }
            else break;
        }
    }

    private static void pushIndoorButton() {
        String keyInput;
        Scanner scanner = new Scanner(System.in);
        int push;

        while(true) {
            System.out.print("실내버튼(층수) 버튼안누름(엔터)");

            keyInput = scanner.nextLine();

            if(!keyInput.isEmpty()) {
                push = Integer.parseInt(keyInput);

                int index = Math.abs(push) - 1;

                if(index >= 0 && index < stops.length) {
                    if(push > floor) stops[index] = stops[index] == DOWN ? BOTH : UP;
                    else if(push < floor) stops[index] = stops[index] == UP ? BOTH : DOWN;
                    break;
                }
                else System.out.println(">> 올바른 층수를 입력해주세요.");
            }
            else break;
        }
    }

    private static void initStops() {
        for(int i = 0; i < stops.length; i++) {
            stops[i] = NONE;
        }
    }

    private static boolean shouldOpenDoor() {
        if(direction == UP) {
            if(stops[floor - 1] == UP || stops[floor - 1] == BOTH) return true;
        }
        else if(direction == DOWN) {
            if(stops[floor - 1] == DOWN || stops[floor - 1] == BOTH) return true;
        }
        else if(direction == STOP) {
            if(stops[floor - 1] != NONE) return true;
        }

        return false;
    }

    private static boolean shouldStop() {
        for(int i = 0; i < stops.length; i++) {
            if(stops[i] != NONE) return false;
        }

        return true;
    }

    private static void decideDirection() {
        for (int i = 0; i < stops.length; i++) {
            if (stops[i] != NONE) {
                int floorToGo = i + 1;

                if (floor < floorToGo) direction = UP;
                else if (floor > floorToGo) direction = DOWN;
                break;
            }
        }
    }

    private static boolean shouldGoUp() {
        for(int i = floor; i < stops.length; i++) {
            if(stops[i] != NONE) return true;
        }

        return false;
    }

    private static boolean shouldGoDown() {
        for(int i = floor - 2; i >= 0; i--) {
            if(stops[i] != NONE) return true;
        }

        return false;
    }

    private static void turnOffUpButton() {
        if(stops[floor - 1] == BOTH) stops[floor - 1] = DOWN;
        else if(stops[floor - 1] == UP) stops[floor - 1] = NONE;
    }

    private static void turnOffDownButton() {
        if(stops[floor - 1] == BOTH) stops[floor - 1] = UP;
        else if(stops[floor - 1] == DOWN) stops[floor - 1] = NONE;
    }

    private static void printStops() {
        for(int i = 0; i < stops.length; i++) {
            System.out.print(stops[i] + " ");
        }

        System.out.println();
    }
}