package it.unibo.ai.didattica.competition.tablut.belugaClient;

import java.io.IOException;
import java.net.UnknownHostException;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.*;

/**
 *
 * @author A. Piretti, Andrea Galassi
 *
 */
public class TablutBelugaClient extends TablutClient {

    public TablutBelugaClient(String player, String name, int gameChosen, int timeout, String ipAddress) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
    }

    public TablutBelugaClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
        this(player, name, 4, timeout, ipAddress);
    }

    public TablutBelugaClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
        this(player, "Beluga", 4, timeout, ipAddress);
    }

    public TablutBelugaClient(String player) throws UnknownHostException, IOException {
        this(player, "Beluga", 4, 60, "localhost");
    }


    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int gametype = 4;
        String role = "";
        String name = "Beluga";
        String ipAddress = "localhost";
        int timeout = 60;
        // TODO: change the behavior?
        if (args.length < 1) {
            System.out.println("You must specify which player you are (WHITE or BLACK)");
            System.exit(-1);
        } else {
            System.out.println(args[0]);
            role = (args[0]);
        }
        if (args.length == 2) {
            System.out.println(args[1]);
            timeout = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            ipAddress = args[2];
        }
        System.out.println("Selected client: " + args[0]);

        TablutBelugaClient client = new TablutBelugaClient(role, name, gametype, timeout, ipAddress);
        client.run();
    }

    @Override
    public void run() {

        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE);
        GameAshtonTablut rules = new GameAshtonTablut(0, -1, "logs", "white", "black");
        System.out.println("Ashton Tablut game");


        System.out.println("\n"+
                        "+------------------------------ Ashton Tablut game challenge 2022 ------------------------------+");
        System.out.println(
                        "|	                                                                                        |\n"+
                        "|	...,**/*/*//*//**/,............................................,..,,,,,////*****	|\n"+
                        "|	..,,,****///////***/...................................,..........,,/**////*****	|\n"+
                        "|	..*,,,*//////////**/**,....,...,..................,,..,..........,,,,*////*/****	|\n"+
                        "|	,,,*,,*////(((/////**/*/*,,,,,,,.,.,.................,,,,....,,,,,,,*****//*/***	|\n"+
                        "|	..,***,*//////////(((((((((((####(((((*((/,,,/**,.,..,.....,,,...**//////////***	|\n"+
                        "|	.,.,*/****///(((((((((((/(((((/(##############/,,.,,,,.  ... ...,****//*//**////	|\n"+
                        "|	..,,*//((((/(((((((////((#(####%################*,**,.... .....,,********///////	|\n"+
                        "|	  .,///*/((/(((/(//(*((################%%#%######((****.,..  ..,,,,,***///*/*///	|\n"+
                        "|	..,//*//(///////*/(((((((#######%%%%#%%%%%%#########((/*,,,.. ...,,,*****/*/////	|\n"+
                        "|	.../(////////**/////*/((############%%%%%%%%##########((/**,,.. . .,,,*///*/*//(	|\n"+
                        "|	..*((#(((/*/****,  ....**/(#########%%%%%%########(#####((/**..... .,,***//////*	|\n"+
                        "|	*((((((((((/*. ,.       ../(##((######%######(((##((*.   ...,,.......,.,***/(///	|\n"+
                        "|	(((((((####(,.*   .    ..,.(##(((#########((((##(* ...  ...  .........,,**/((//*	|\n"+
                        "|	((((((((####(,*.       .,,,((##(((###(####(((((/ ,,   ..   ,, ...... ....,///*,,	|\n"+
                        "|	((((((((#######(*,,,,,*,,./(((((((((((#((#((#((,,*./*       *. **.     ... .,,**	|\n"+
                        "|	(((((((((############((((((((((((((((((((((((((/..,*.     .,* */*,,,,.    ...,/(	|\n"+
                        "|	((((((((((((#(######((((((((((/(((///(((((((((((((((##########(/**,,..... ....,*	|\n"+
                        "|	/(/(/////((((((((((((((((///////////(((////((((#(###############(//**,**,...  .,	|\n"+
                        "|	////////(((((((((((((((((/*,,,.*//////////***/((((((((((##########((((((/,.  ..,	|\n"+
                        "|	/////////////////(((((((((((/*******//,..,*/(((((((((((((((((((((((((((((/,,....	|\n"+
                        "|	/////*///*////////////////((///********//((((((((((((((((((((((/*//((((((((/*,.,	|\n"+
                        "|	////******************/*///////**,,*////////((///(((//////((((///**//(((((((///(	|\n"+
                        "|	/************,,,,..,,************,,*******////////*****//*/////////*//(((((#(((#	|\n"+
                        "|	***************,,,**,,*,,,,,,,,,,,,,,,,,,,,,,******************/////(((((((#####	|\n"+
                        "|	***/*********************************,,,,***,,,*,,,,,,,********/////(((((((#####	|\n"+
                        "|	**/**//*/******************************************,,,,,******////*///((((((####	|\n"+
                        "|	/*****/*///****************************************,,,,******///*//////(((((####	|\n"+
                        "|	*****////////******************************,**,****************/////////(((((###	|\n"+
                        "|	/******///////////**********************,,,,,,*,**,***********////////(((((((###	|\n"+
                        "|	//**********/////////****************,,,,,,,,,,***********/////////((((((((#####	|\n"+
                        "|	///**************///******************,***************///////////(((((((((######	|\n"+
                        "|	//////*******************************************/*//////////((//(((((((((######	|\n"+

                        "|	                                                                                        |\n"+
                        "|	  ███████████  ██████████ █████       █████  █████   █████████    █████████  		|\n"+
                        "|	 ░░███░░░░░███░░███░░░░░█░░███       ░░███  ░░███   ███░░░░░███  ███░░░░░███  		|\n"+
                        "|	  ░███    ░███ ░███  █ ░  ░███        ░███   ░███  ███     ░░░  ░███    ░███  		|\n"+
                        "|	  ░██████████  ░██████    ░███        ░███   ░███ ░███          ░███████████  		|\n"+
                        "|	  ░███░░░░░███ ░███░░█    ░███        ░███   ░███ ░███    █████ ░███░░░░░███  		|\n"+
                        "|	  ░███    ░███ ░███ ░   █ ░███      █ ░███   ░███ ░░███  ░░███  ░███    ░███  		|\n"+
                        "|	  ███████████  ██████████ ███████████ ░░████████   ░░█████████  █████   █████ 		|\n"+
                        "|	 ░░░░░░░░░░░  ░░░░░░░░░░ ░░░░░░░░░░░   ░░░░░░░░     ░░░░░░░░░  ░░░░░   ░░░░░ 		|\n" +
                        "|	                                                                                        |");


        System.out.println(
                        "+------------------------- Made by Lorenzo Di Palma, Giovanni Gheriglio ------------------------+\n");

        // attributes depends to parameters passed to main
        System.out.println("Player: " + (this.getPlayer().equals(State.Turn.BLACK) ? "BLACK" : "WHITE" ));
        System.out.println("Timeout: " + this.getTimeout() +" s");
        System.out.println("Server: " + this.getServerIp());

        while (true) {
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }
            System.out.println("Current state:");
            state = this.getCurrentState();
            System.out.println(state.toString());


            if (this.getPlayer().equals(State.Turn.WHITE)) {
                // My turn: WHITE
                if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {

                    Action a = searchForBest(rules, state);


                    System.out.println("Chosen move: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
                // Opponent's turn: BLACK
                else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
                    System.out.println("Waiting for your opponent move... ");
                }
                // Win
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }
                // Lost
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }
                // Draw
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }

            } else {

                // My turn: BLACK
                if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {

                    Action a = searchForBest(rules, state);

                    System.out.println("Chosen move: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
                // Opponent's turn: WHITE
                else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    System.out.println("Waiting for your opponent move... ");
                }
                // Lost
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }
                // Win
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }
                // Draw
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }

            }
        }

    }

    private Action searchForBest(GameAshtonTablut rules, State state)
    {
        IterativeDeepeningAlphaBetaSearchWithHeuristic search = new IterativeDeepeningAlphaBetaSearchWithHeuristic(rules, Double.MIN_VALUE, Double.MAX_VALUE, this.getTimeout() - 2);
        return search.makeDecision(state);
    }

}