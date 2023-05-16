/*
Aaron Kimbel
ICS4U0-C
Final Project
Animal Kingdom: Card Arena
Displays all of the cards, can be panned up or down
*/

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class Board extends JPanel{
    public int x = 0;
    public float y = -PanelManager.ScreenHeight;
    public int width = PanelManager.ScreenWidth;
    public int height = PanelManager.ScreenHeight*2;

    public Board() {
        this.setLayout(null);
        this.setBounds(x,(int) y,width,height);

        drawNewCard(0);
        drawNewCard(1);
        drawNewCard(2);
        drawNewCard(3);
        drawNewCard(4);

        createDrop(PanelManager.center-PanelManager.CardWidth*2-PanelManager.spacing*2,PanelManager.cardY+PanelManager.ScreenHeight/3,0);
        createDrop(PanelManager.center-PanelManager.CardWidth-PanelManager.spacing,PanelManager.cardY+PanelManager.ScreenHeight/3,1);
        createDrop(PanelManager.center,PanelManager.cardY+PanelManager.ScreenHeight/3,2);
        createDrop(PanelManager.center+PanelManager.CardWidth+PanelManager.spacing,PanelManager.cardY+PanelManager.ScreenHeight/3,3);
        createDrop(PanelManager.center+PanelManager.CardWidth*2+PanelManager.spacing*2,PanelManager.cardY+PanelManager.ScreenHeight/3,4);

		/*
		//TODO these should be fake drop points, not real ones
		this.add(createDrop(PanelManager.center-PanelManager.CardWidth*2-PanelManager.spacing*2,PanelManager.cardY-PanelManager.cardY/16));
		this.add(createDrop(PanelManager.center-PanelManager.CardWidth-PanelManager.spacing,PanelManager.cardY-PanelManager.cardY/16));
		this.add(createDrop(PanelManager.center,PanelManager.cardY-PanelManager.cardY/16));
		this.add(createDrop(PanelManager.center+PanelManager.CardWidth+PanelManager.spacing,PanelManager.cardY-PanelManager.cardY/16));
		this.add(createDrop(PanelManager.center+PanelManager.CardWidth*2+PanelManager.spacing*2,PanelManager.cardY-PanelManager.cardY/16));
		*/
    }
    public PlayerCardPanel createCard(int x, int y, int pos, Card card) {
        PlayerCardPanel c = new PlayerCardPanel(card);
        c.setX(x);
        c.setY(y);
        PanelManager.player.PlayerHand[pos] = c;
        return c;
    }
    public DropLocation createDrop(int x, int y, int pos) {
        DropLocation d = new DropLocation(x,y,pos);
        PanelManager.dropLocations[pos] = d;
        this.add(d);
        this.setComponentZOrder(d, 5);
        return d;
    }

    public PlayerCardPanel drawNewCard(int pos) {
        assert pos >= 0 && pos <=4;
        int x = switch (pos) {
            case 0 -> PanelManager.center - PanelManager.CardWidth * 2 - PanelManager.spacing * 2;
            case 1 -> PanelManager.center - PanelManager.CardWidth - PanelManager.spacing;
            case 2 -> PanelManager.center;
            case 3 -> PanelManager.center + PanelManager.CardWidth + PanelManager.spacing;
            case 4 -> PanelManager.center + PanelManager.CardWidth * 2 + PanelManager.spacing * 2;
            default -> 0;
        };

        PlayerCardPanel c = createCard(x,PanelManager.cardY+PanelManager.ScreenHeight+PanelManager.CardHeight, pos, CardDeck.drawCard());
        this.add(c);
        this.setComponentZOrder(c,0);
        c.smoothTransition(new Point(c.x,c.y-PanelManager.CardHeight));
        return c;
    }



    public boolean showHand = false;
    int destination = 0;
    public void move() { //the board to show either the players hand or the play area
        Timer timer = new Timer();

        if(showHand) destination = -PanelManager.ScreenHeight; //set destination to hand
        else destination = -PanelManager.ScreenHeight/2; //set destination to board

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                y = (float) PanelManager.lerp(y,destination,0.1);
                if(showHand ? y > destination-1 : y < destination+1) { //if lastY is the same lerp is stuck due to rounding errors between int and double
                    y = destination;
                    setBounds(x,(int) y,width,height);
                    this.cancel();
                }
                setBounds(x,(int) y,width,height);
            }
        }, 1, 10);

        showHand = !showHand; //inverts showHand
    }

    public void moveUp() {
        if(!showHand) {
            move();
        }
    }
    public void moveDown() {
        if(showHand) {
            move();
        }
    }
}
