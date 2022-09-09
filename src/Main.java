import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


public class Main extends JFrame
                       implements ActionListener
{
    protected boolean gameOn = true;
    protected boolean gameDraw = false;
    protected char playerSymbol = 'O';
    protected int clicks = 0;
    protected int winnerLine[] = new int[3]; // Array of the three matched symbols

    protected final Color oPlayerColor = new Color(18, 164, 217);
    protected final Color xPlayerColor = new Color(217, 19, 138);
    protected final Color backGColor = new Color(255, 234, 0);
    protected final Color foreGColor = new Color(50, 46, 47);
    protected final Font headerFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    protected final Font gridFont = new Font(Font.SANS_SERIF, Font.BOLD, 35);
    protected final Font footerFont = new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12);

    protected JLabel header = new JLabel("Player "+playerSymbol+" starts", SwingConstants.CENTER);
    protected JPanel gridPanel = new JPanel(); 
    protected JButton gridButtons[] = new JButton[9];
    protected JButton footerBtn = new JButton("SWAP");


    protected final boolean isGameOver(){
        // Row isGameOvers
        for(int i = 0; i<7; i+=3){
            if (!gridButtons[i].getText().isEmpty()){
                if (gridButtons[i].getText().equals(gridButtons[i+1].getText()) && gridButtons[i+1].getText().equals(gridButtons[i+2].getText())){
                    // System.out.println("\n"+(i/3 + 1)+" row Matched");
                    winnerLine[0] = i; winnerLine[1] = i+1; winnerLine[2] = i+2;
                    return true;
                }
            }
        }
        // Column isGameOvers
        for(int i = 0; i<3; i++){
            if (!gridButtons[i].getText().isEmpty()){
                if (gridButtons[i].getText().equals(gridButtons[i+3].getText()) && gridButtons[i+3].getText().equals(gridButtons[i+6].getText())){
                    // System.out.println("\n"+(i+1)+" column Matched");
                    winnerLine[0] = i; winnerLine[1] = i+3; winnerLine[2] = i+6;
                    return true;
                }
            }
        }
        // Diagonal isGameOvers
        for(int i = 0; i<3; i+=2){
            if (!gridButtons[i].getText().isEmpty()){
                if (i == 0){
                    if (gridButtons[i].getText().equals(gridButtons[i+4].getText()) && gridButtons[i+4].getText().equals(gridButtons[i+8].getText())){
                        // System.out.println("\n- Diagonal Matched");
                        winnerLine[0] = 0; winnerLine[1] = 4; winnerLine[2] = 8;
                        return true;
                    }
                }
                else {
                    if (gridButtons[i].getText().equals(gridButtons[i+2].getText()) && gridButtons[i+2].getText().equals(gridButtons[i+4].getText())){
                        // System.out.println("\n+ Diagonal Matched");
                        winnerLine[0] = 2; winnerLine[1] = 4; winnerLine[2] = 6;
                        return true;
                    }
                }
            }
        }

        return false;
    }
    

    protected final void gameOver(String msg){
        // Altering Header Section
        header.setText(msg);
        header.setEnabled(true);
        if (!gameDraw) header.setForeground( (playerSymbol == 'X')? xPlayerColor : oPlayerColor );
        
        // Altering TicTacToe Panel
        for (int i = 0; i<9; i++){
            if (gameDraw || ( i != winnerLine[0] && i != winnerLine[1] && i != winnerLine[2]) ){
                gridButtons[i].setEnabled(false);
            }
        }

        // Altering Footer Section
        footerBtn.setEnabled(true);
        footerBtn.setText("REPLAY");
        footerBtn.setToolTipText("Restart Game");
    }
    

    protected final void replay(){
        gameOn = true;
        gameDraw = false;
        playerSymbol = 'O';
        clicks = 0;

        // Resetting Header Section
        header.setForeground(foreGColor);
        header.setText("Player "+playerSymbol+" starts");
        header.setEnabled(true);
        
        // Resetting TicTacToe Panel
        for (int i = 0; i<9; i++){
            gridButtons[i].setEnabled(true);
            gridButtons[i].setText("");
            gridButtons[i].setActionCommand(i+"");
            gridButtons[i].removeActionListener(this);
            gridButtons[i].addActionListener(this);
        }
        
        // Resetting Footer Section
        footerBtn.setText("SWAP");
        footerBtn.setToolTipText("Change Player");
    }


    protected final void swapPlayer(){
        playerSymbol = (playerSymbol == 'O')?'X':'O';
    }


    public Main(){
        // Setting JFrame
        setVisible(true);
        setSize(340,412);
        setTitle("TicTacToe by Om Gupta");
        URL iconURL = Main.class.getResource("icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(backGColor);

        // Setting Header Section
        header.setForeground(foreGColor);
        header.setFont(headerFont);
        header.setBorder(new EmptyBorder(20,0,0,0));
        add(header, BorderLayout.NORTH);
        
        // Setting TicTacToe Panel Section
        gridPanel.setBackground(foreGColor);
        gridPanel.setLayout(new GridLayout(3,3,5,5));
        gridPanel.setBorder(BorderFactory.createLineBorder(backGColor,20));
        for (int i = 0; i<9; i++){
            gridButtons[i] = new JButton("");
            gridButtons[i].setActionCommand(i+"");
            gridButtons[i].addActionListener(this);
            gridButtons[i].setBackground(backGColor);
            gridButtons[i].setBorderPainted(false);
            gridButtons[i].setFocusPainted(false);
            gridButtons[i].setFont(gridFont);
            gridPanel.add(gridButtons[i]);
        }
        add(gridPanel, BorderLayout.CENTER);
        
        // Setting Footer Section
        footerBtn.addActionListener(this);
        footerBtn.setBackground(foreGColor);
        footerBtn.setForeground(Color.WHITE);
        footerBtn.setFocusPainted(false);
        footerBtn.setFont(footerFont);
        footerBtn.setBorder(new EmptyBorder(10,0,10,0));
        footerBtn.setToolTipText("Change Player");
        add(footerBtn, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        
        if (str.equals("SWAP")){
            swapPlayer();
            header.setText("Player "+playerSymbol+" starts");
        }
        else if (str.equals("REPLAY")){
            replay();
        }
        else {
            header.setEnabled(false);
            footerBtn.setEnabled(false);
            gridButtons[Integer.parseInt(str)].removeActionListener(this);
            gridButtons[Integer.parseInt(str)].setText(playerSymbol+"");
            gridButtons[Integer.parseInt(str)].setForeground( (playerSymbol == 'X')? xPlayerColor : oPlayerColor );
            
            clicks++;
            if (clicks > 4 ){
                if (isGameOver() || clicks == 9){
                    gameOn = false;
                }
            }

            if (gameOn){
                swapPlayer();
                header.setText("Player "+playerSymbol+"'s Turn");
            } else {
                if (!isGameOver() && clicks == 9){
                    gameDraw = true;
                    gameOver("Game Draw");
                }
                else gameOver(playerSymbol+" won the game");
            }
            
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new Main();
            }   
        });
    }
}
