import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextArea originalTextArea;
    private JTextArea summaryTextArea;

    public MainFrame() {
        setTitle("Resumidor de Documentos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        originalTextArea = new JTextArea("Arraste o documento aqui...");
        summaryTextArea = new JTextArea("O resumo aparecerá aqui.");

        originalTextArea.setLineWrap(true);
        summaryTextArea.setLineWrap(true);
        originalTextArea.setEditable(false);
        summaryTextArea.setEditable(false);

        // Configura a funcionalidade de Drag and Drop
        new DropTarget(originalTextArea, new FileDropListener());

        add(new JScrollPane(originalTextArea));
        add(new JScrollPane(summaryTextArea));

        setLocationRelativeTo(null);
    }

    // Classe interna para ouvir o evento de arrastar arquivo
    private class FileDropListener extends DropTargetAdapter {
        @Override
        public void drop(DropTargetDropEvent event) {
            try {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                List<File> droppedFiles = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                if (!droppedFiles.isEmpty()) {
                    File file = droppedFiles.get(0); // Pega apenas o primeiro arquivo
                    processFile(file);
                }
            } catch (Exception ex) {
                originalTextArea.setText("Erro ao ler o arquivo: \n" + ex.getMessage());
            }
        }
    }

    private void processFile(File file) {
        try {
            originalTextArea.setText("Lendo o arquivo: " + file.getName() + "\n\nAguarde...");

            // 1. Extrair o texto
            String fullText = DocumentExtractor.extractText(file);
            originalTextArea.setText(fullText);

            // 2. Resumir o texto
            String summary = TextSummarizer.summarize(fullText, 5); // Resumir em 5 frases
            summaryTextArea.setText(summary);

        } catch (Exception e) {
            originalTextArea.setText("Não foi possível processar o arquivo.\nErro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}