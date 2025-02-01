import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class PasswordGenerator extends JFrame {

    private JTextField passwordLengthField;
    private JComboBox<CharacterType> charTypesBox;
    private JButton generateButton;

    public enum CharacterType {
        NUMBER, UPPERCASE_LETTER, LOWERCASE_LETTER, SPECIAL_CHARACTER
    }

    public PasswordGenerator() {
        super("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);

        JLabel passwordLengthLabel = new JLabel("Password Length:");
        passwordLengthField = new JTextField(10);
        JButton lengthErrorButton = new JButton("Check");
        lengthErrorButton.addActionListener(e -> {
            try {
                int length = Integer.parseInt(passwordLengthField.getText());
                if (length < 8) {
                    JOptionPane.showMessageDialog(this, "Password length must be at least 8 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid password length. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        northPanel.add(passwordLengthLabel);
        northPanel.add(passwordLengthField);
        northPanel.add(lengthErrorButton);

        JPanel southPanel = new JPanel();
        add(southPanel, BorderLayout.SOUTH);

        JLabel charTypeLabel = new JLabel("Select Character Type:");
        charTypesBox = new JComboBox<>(CharacterType.values());
        southPanel.add(charTypeLabel);
        southPanel.add(charTypesBox);

        generateButton = new JButton("Generate Password");
        JPanel centerPanel = new JPanel();
        centerPanel.add(generateButton);
        add(centerPanel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> {
            try {
                int passwordLength = Integer.parseInt(passwordLengthField.getText());
                Random random = new Random();
                StringBuilder password = new StringBuilder(passwordLength);

                char[] numbers = "0123456789".toCharArray();
                char[] uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                char[] lowercaseLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                String specialCharacters = "!@#$%^&*()";

                CharacterType selectedType = (CharacterType) charTypesBox.getSelectedItem();

                for (int i = 0; i < passwordLength; i++) {
                    switch (selectedType) {
                        case NUMBER:
                            password.append(numbers[random.nextInt(numbers.length)]);
                            break;
                        case UPPERCASE_LETTER:
                            password.append(uppercaseLetters[random.nextInt(uppercaseLetters.length)]);
                            break;
                        case LOWERCASE_LETTER:
                            password.append(lowercaseLetters[random.nextInt(lowercaseLetters.length)]);
                            break;
                        case SPECIAL_CHARACTER:
                            password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
                            break;
                    }
                }
                JOptionPane.showMessageDialog(this, "Generated Password: " + password);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid password length. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(400, 200);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new PasswordGenerator().setVisible(true);
    }
}
