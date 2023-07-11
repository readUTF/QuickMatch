package com.readutf.server.commands;

import java.util.Scanner;

public class CommandManager  {

    public CommandManager(Command... commands) {
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {

            while (scanner.hasNext()) {
                String in = scanner.nextLine();
                try {
                    String[] args = in.split(" ");
                    boolean ran = false;
                    for (Command command : commands) {
                        String[] commandArgs = new String[args.length - 1];
                        if(args.length > 1) System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
                        if(command.getName().equalsIgnoreCase(args[0])) {
                            if(command.getMinArgs() > commandArgs.length) {
                                System.out.println("Usage: " + command.getUsage());
                                return;
                            }
                            command.onCommand(commandArgs);
                            ran = true;
                        }

                    }
                    if(!ran) System.out.println("Command not found!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }
}
