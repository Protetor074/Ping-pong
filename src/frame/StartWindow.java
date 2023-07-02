package frame;

import error.ErrorOddParity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame {

    private JPanel boardSizeParametrs;

    private JSpinner boardSize[];
    JLabel boardSizeLabel[];
    private JButton setValue;

    private final int yDistance = 15;//odstęp w osi y
    private final int xDistance = 15;//odstęp w osi x
    private final int widthWindow = 300;
    private final int heightWindow = 240;


    public StartWindow(String name) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        createBoard();
        setActionButton();
    }

    public void createBoard() {

        final int border = 10;

        setSize(widthWindow, heightWindow);
        boardSizeParametrs = new JPanel();
        setContentPane(boardSizeParametrs);
        setLayout(null);
        boardSizeParametrs.setLayout(null);
        setResizable(false);

        //plansza parametrow
        JPanel parameters = new JPanel();
        boardSizeParametrs.add(parameters);
        parameters.setBounds(xDistance, yDistance / 2, widthWindow - 3 * yDistance, heightWindow - 4 * xDistance);
        parameters.setBorder(BorderFactory.createTitledBorder("Parametry"));
        parameters.setLayout(null);

        //ustawienia etykiet pól wartości
        boardSizeLabel = new JLabel[]{
                new JLabel("Liczba kolumn:"),
                new JLabel("Liczba wierszy:"),
                new JLabel("Należy pamiętać że wartość liczby kolumn ma być wartością niepaarzystą\n a liczba wierszy powina być większa od 10.")
        };

        //ustawienia pól wartości
        SpinnerNumberModel S1 = new SpinnerNumberModel(13, 11, 41, 1);
        SpinnerNumberModel S2 = new SpinnerNumberModel(13, 10, 41, 1);
        boardSize = new JSpinner[]{
                new JSpinner(S1),
                new JSpinner(S2)
        };

        for (int i = 0; i < boardSize.length; i++) {
            boardSizeLabel[i].setBounds(border, 3 * border + i * (3 * border), parameters.getWidth() / 2, parameters.getHeight() / 8);
            boardSize[i].setBounds(border + parameters.getWidth() / 2, 3 * border + i * (3 * border), parameters.getWidth() / 5, parameters.getHeight() / 8);
            parameters.add(boardSizeLabel[i]);
            parameters.add(boardSize[i]);
        }


        setValue = new JButton("Generuj");
        setValue.setBounds(parameters.getWidth() / 2 - parameters.getWidth() / 4, border * 10, parameters.getWidth() / 2, parameters.getWidth() / 4);
        parameters.add(setValue);
    }

    private void setActionButton() {
        setValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ((int) boardSize[0].getValue() % 2 != 0) {
                    Frame frame = new Frame("Symulacja", (int) boardSize[0].getValue(), (int) boardSize[1].getValue());
                } else {
                    try {
                        throw new ErrorOddParity();
                    } catch (ErrorOddParity ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
    }

}
