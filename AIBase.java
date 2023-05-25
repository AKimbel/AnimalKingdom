/*
Name: Mateo Adebowale
Class: ICS4U0
Assignment: Animal Kingdom: Card Arena
Sub-Assignment: AI base object
Date: 2023-05-16
Description: The main hub where all the AI values will be stored and strategy types will be decided
*/
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class AIBase
{
    //--------------------------------------------------
    //note: these should not be static, static means the variable is global across all instances of the object
    //in this case, the ai is an object, don't access it with AIBase, access it through PanelManager.ai, or have it be public in main
    protected LinkedList<Card> AiHandList = new LinkedList<Card>();
    protected int HP, playedVP = 0, ran;
    private final int maxHP;
    //--------------------------------------------------
    public AIBase()
    {
        for (int i = 0; i < 5; i++)
        {
            AiHandList.add(CardDeck.drawCard());
        }
        HP = maxHP = 150;
    }
    //--------------------------------------------------
    public void playAI()
    {
        playedVP = 0;
        ran = (int)((Math.random()*100) + 1);
        if (ran > 0 && ran < 16)
        {
            AiHandList = RecklessAI.Play(AiHandList);
            move();
        }
        else
        {
            if (HP <= (Player.getHP() - 20))
            {
                AiHandList = DefensiveAI.Play(AiHandList);
                move();
            }
            else
            {
                AiHandList = AggressiveAI.Play(AiHandList);
                move();
            }
        }
    }
    //--------------------------------------------------
    private void move()
    {
        for (int i = 0; i < 5; i++)
        {
            if ((playedVP + AiHandList.get(i).getVP()) <= 5)
            {
                for (int ii = 0; ii < 5; ii++)
                {
                    if (AICardManager.AIPlayed[ii] == null)
                    {
                        AICardManager.playCard(AiHandList.get(i), ii);
                        AiHandList.set(i, CardDeck.drawCard());
                        playedVP = playedVP + AiHandList.get(i).getVP();
                    }
                }
            }
            else
            {
                break;
            }
        }
    }

    private void attack() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int x;
            @Override
            public void run() {
                if(AICardManager.AIPlayed[x] != null )
                    AICardManager.AIPlayed[x].attack();
                x++;
                if(x > AICardManager.AIPlayed.length-1) {
                    this.cancel();
                }
            }
        },200,100);
    }
    //--------------------------------------------------
    public int getHP()
    {
        return this.HP;
    }
}