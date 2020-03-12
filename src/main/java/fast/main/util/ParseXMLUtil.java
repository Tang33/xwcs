package fast.main.util;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.crypto.Data;


public class ParseXMLUtil {

    private  final BlockingQueue<Map<String,String>> results ;

    public ParseXMLUtil(BlockingQueue<Map<String,String>> results) {
        this.results = results;
    }

    public void processFirstSheet(String filename) throws Exception {

        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        try {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            // process the first sheet
            InputStream sheet2 = r.getSheetsData().next();
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
            sheet2.close();
        } finally {
            pkg.close();
        }
    }

    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        try {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
        } finally {
            pkg.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst,this.results);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean inlineStr;
        private String row ="";
        private final  BlockingQueue<Map<String,String>> results;
        private  Map<String,String> dto  = new HashMap<String,String>();
        private final LruCache<Integer,String> lruCache = new LruCache<Integer,String>(50);

        private static class LruCache<A,B> extends LinkedHashMap<A, B> {
            private final int maxEntries;

            public LruCache(final int maxEntries) {
                super(maxEntries + 1, 1.0f, true);
                this.maxEntries = maxEntries;
            }

            @Override
            protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
                return super.size() > maxEntries;
            }
        }

        private SheetHandler(SharedStringsTable sst,final BlockingQueue<Map<String,String>> results) {
            this.sst = sst;
            this.results = results;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                row = attributes.getValue("r") ;
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                inlineStr = cellType != null && cellType.equals("inlineStr");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v") || (inlineStr && name.equals("c"))) {
                char rowC=row.substring(0,1).charAt(0);
                int  rowNum=Integer.parseInt(row.substring(1));
                if(rowNum <=1){
                    return;
                }
            }
            if(name.equals("row")){
            int  rowNum=Integer.parseInt(row.substring(1));
            if(rowNum <=1){
                return;
            }
              results.offer(dto);
              dto = new HashMap<String,String>();

            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
        }
    }

    public static void main(String[] args) throws Exception {
        long startMini = System.currentTimeMillis();
        BlockingQueue<Map<String,String>> results = new ArrayBlockingQueue<Map<String,String>>(16);
        ParseXMLUtil xmlUtil = new ParseXMLUtil(results);
        xmlUtil.processFirstSheet("C:/Users/92039/Desktop/print/demo2.xlsx");
        System.out.println(results.size());
        long endMini = System.currentTimeMillis();
        System.out.println(" take time is " + (endMini - startMini)/1000.0);
    }
}
