import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {

        Player player = new Player();

        GameFrame frame = new GameFrame();
        frame.setVisible(true);

        PanelManager.start(frame,player);

        Timer timer = new Timer();
        //this is very stupid and im acknowledging that and doing it regardless, it works.
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                frame.repaint();
                frame.revalidate();
            }
        }, 1, 1);
    }
}
