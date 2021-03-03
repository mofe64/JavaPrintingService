package nubari;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class PrintingService {
    private final DocFlavor flavor;
    private final PrintRequestAttributeSet printRequestAttributeSet;
    private OrientationRequested orientationRequested;
    private int numberOfCopies;
    private PrintService defaultPrintService;
    private PrintService printServiceToUse;
    private PrintService[] printServices;
    private FileInputStream fileInputStream;
    private DocPrintJob printJob;

    public PrintingService() {
        printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printServices = PrintServiceLookup.lookupPrintServices(null, null);
        numberOfCopies = 1;
        defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        orientationRequested = OrientationRequested.PORTRAIT;
        flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        printServiceToUse = defaultPrintService;
    }

    public void setCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void changeOrientation(OrientationRequested orientation) {
        this.orientationRequested = orientation;
    }

    public void selectPrintingService() {
        printRequestAttributeSet.add(orientationRequested);
        printRequestAttributeSet.add(new Copies(numberOfCopies));
        this.printServiceToUse = ServiceUI.printDialog(null, 150, 150, printServices, defaultPrintService,
                flavor, printRequestAttributeSet);
    }

    public DocPrintJob createPrintJob() {

        printJob = printServiceToUse.createPrintJob();
        printJob.addPrintJobListener(new PrintJobAdapter() {
            @Override
            public void printDataTransferCompleted(PrintJobEvent pje) {
                System.out.println("printDataTransferCompleted");
                System.out.println(pje.getPrintEventType());
                System.out.println(pje.toString());

            }

            @Override
            public void printJobCompleted(PrintJobEvent pje) {
                System.out.println("printJobCompleted");
                System.out.println(pje.getPrintEventType());
                System.out.println(pje.toString());
            }

            @Override
            public void printJobFailed(PrintJobEvent pje) {
                System.out.println("printJobFailed");
                System.out.println(pje.getPrintEventType());
                System.out.println(pje.toString());
            }

            @Override
            public void printJobCanceled(PrintJobEvent pje) {
                System.out.println("printJobCanceled");
                System.out.println(pje.getPrintEventType());
                System.out.println(pje.toString());
            }

            @Override
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                System.out.println("PrintJobEvent: printJobNoMoreEvents");
                System.out.println(pje.getPrintEventType());
                System.out.println(pje.toString());
            }

            @Override
            public void printJobRequiresAttention(PrintJobEvent pje) {
                System.out.println("Print Job Requires Attention !!!");
            }
        });
        return printJob;
    }

    public void print(String filePath) throws IOException, PrintException {
        fileInputStream = new FileInputStream(filePath);
        System.out.println(fileInputStream.toString());
        Doc document = new SimpleDoc(fileInputStream, flavor, null);
        printJob = createPrintJob();
//        printJob = printServiceToUse.createPrintJob();
        printJob.print(document, printRequestAttributeSet);
        fileInputStream.close();
    }

    public void print() throws IOException, PrintException {

        FileChooserObj fileChooser = new FileChooserObj();
        fileChooser.setSize(400,400);
        fileChooser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileChooser.setVisible(true);
        Path path = fileChooser.getFileOrDirectoryPath();
        System.out.println(path.toString());
        fileInputStream = new FileInputStream(path.toString());
        System.out.println(fileInputStream.toString());
        Doc document = new SimpleDoc(fileInputStream, flavor, null);
        printJob = createPrintJob();
//        printJob = printServiceToUse.createPrintJob();
        printJob.print(document, printRequestAttributeSet);
        fileInputStream.close();
    }

    private static class FileChooserObj extends JFrame {
        private final JTextArea outputArea;

        public FileChooserObj() {
            super("Printing Service");
            outputArea = new JTextArea();
            add(new JScrollPane(outputArea));
        }

        public Path getFileOrDirectoryPath() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(
                    JFileChooser.FILES_AND_DIRECTORIES
            );
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.CANCEL_OPTION) {
                return null;
            }
            return fileChooser.getSelectedFile().toPath();
        }
    }

}