
interface Document {
    void open();
    void save();
    String getType();
}

class WordDocument implements Document {
    public void open() {
        System.out.println("Opening Word Document...");
    }

    public void save() {
        System.out.println("Saving Word Document...");
    }

    public String getType() {
        return "Word";
    }
}


class PdfDocument implements Document {
    public void open() {
        System.out.println("Opening PDF Document...");
    }

    public void save() {
        System.out.println("Saving PDF Document...");
    }

    public String getType() {
        return "PDF";
    }
}


class ExcelDocument implements Document {
    public void open() {
        System.out.println("Opening Excel Document...");
    }

    public void save() {
        System.out.println("Saving Excel Document...");
    }

    public String getType() {
        return "Excel";
    }
}


abstract class DocumentFactory {
    public abstract Document createDocument();
}

class WordDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new WordDocument();
    }
}

class PdfDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new PdfDocument();
    }
}

class ExcelDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new ExcelDocument();
    }
}


public class FactoryMethodPatternExample {
    public static void main(String[] args) {

        DocumentFactory wordFactory = new WordDocumentFactory();
        DocumentFactory pdfFactory = new PdfDocumentFactory();
        DocumentFactory excelFactory = new ExcelDocumentFactory();


        Document word = wordFactory.createDocument();
        Document pdf = pdfFactory.createDocument();
        Document excel = excelFactory.createDocument();


        useDocument(word);
        useDocument(pdf);
        useDocument(excel);
    }

    private static void useDocument(Document doc) {
        System.out.println("\n--- Working with " + doc.getType() + " Document ---");
        doc.open();
        doc.save();
    }
}
