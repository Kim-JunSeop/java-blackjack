package gmbs.game;

import gmbs.domain.game.BlackjackGame;
import gmbs.domain.game.GameResult;
import gmbs.domain.participant.*;
import gmbs.view.Command;
import gmbs.view.InputView;
import gmbs.view.OutputView;

import java.util.List;

public class ConsoleGame {

    public void run() {
        BlackjackGame blackjackGame = createBlackjackGame();

        Participants participants = blackjackGame.getParticipants();
        Dealer dealer = participants.getDealer();
        List<Player> players = participants.getPlayers();

        OutputView.printInitialCards(dealer, players);

        playPlayersTurn(blackjackGame, players);
        playDealerTurn(blackjackGame, dealer);

        showGameResult(blackjackGame, dealer, players);
    }

    private BlackjackGame createBlackjackGame() {
        try {
            List<Name> playerNames = InputView.inputPlayerNames();
            return new BlackjackGame(playerNames);
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return createBlackjackGame();
        }
    }

    private void playPlayersTurn(BlackjackGame blackjackGame, List<Player> players) {
        for (Player player : players) {
            playPlayerTurn(blackjackGame, player);
        }
    }

    private void playPlayerTurn(BlackjackGame blackjackGame, Player player) {
        while (!player.isFinished() && !inputCommand(player).isStay()) {
            blackjackGame.drawCard(player);
            OutputView.printCards(player);
        }
    }

    private Command inputCommand(Participant player) {
        try {
            return InputView.inputWantDraw(player.getName());

        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return inputCommand(player);
        }
    }

    private void playDealerTurn(BlackjackGame blackjackGame, Dealer dealer) {
        while (!dealer.isFinished()) {
            OutputView.printDealerDrawInfo();
            blackjackGame.drawCard(dealer);
        }
    }

    private void showGameResult(BlackjackGame blackjackGame, Dealer dealer, List<Player> players) {
        GameResult gameResult = blackjackGame.createGameResult();
        OutputView.printCardsResult(dealer, players);
        OutputView.printGameResult(gameResult);
    }
}
