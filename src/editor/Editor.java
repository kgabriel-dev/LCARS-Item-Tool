package editor;

import main.Main;
import main.Type;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import panels.StatusPanel;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Editor {
    private static Document doc;
    private static Color styleColor;

    public static void editFile() {
        Type type = Storage.getType();

        new Thread(() -> {
            try {

                //Das svg-Dokument laden
                File file = new File("resources/svg/" + type.toString() + ".svg");
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = docBuilder.parse(file.getAbsolutePath());

                StatusPanel.addStatusLine(Main.config.getLanguageWord("statusLoad"));

                String path = Storage.getLocation().getParent() + "/";
                String name = Storage.getLocation().getName();
                name = name.substring(0, name.length() - 4);

                //das normale Bild erstellen
                StatusPanel.addStatusLine(Main.config.getLanguageWord("infoNormalImage"));
                File store = new File(path + name + "_normal.png");
                convertSvgToImage(Storage.getColor(), file, store);

                //das Bild für angeklicktes Item erstellen
                StatusPanel.addStatusLine(Main.config.getLanguageWord("infoClickedImage"));
                store = new File(path + name + "_clicked.png");
                convertSvgToImage(Storage.getClickedColor(), file, store);

            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void convertSvgToImage(Color color, File svg, File output) throws TransformerException {
        setStyleColor(color);

        //Je nach Typ die entsprechende Funktion ausführen
        switch(Storage.getType()) {
            case Button_1Corner: button1Corner(); break;
            case Button_2Corners: button2Corners(); break;
            case Button_Circle: buttonCircle(); break;
            case Button_CutOff: buttonCutOff(); break;
            case Button_Rectangle: buttonRectangle(); break;
            case Item_Bulge: itemBulge(); break;
            case Item_Corner_Right: itemCornerRight(); break;
        }

        StatusPanel.addStatusLine(Main.config.getLanguageWord("statusEdit"));

        //die Änderungen im svg-File speichern
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(svg);
        transformer.transform(source, result);

        StatusPanel.addStatusLine(Main.config.getLanguageWord("statusSave"));

        //das SVG-File als PNG-File speichern
        try {
            storeAsPNG(svg, output);

            //TODO: DER PFAD WIRD OHNE SCHRÄGSTRICHE AUSGEGEBEN
            String statusExport = Main.config.getLanguageWord("statusExport");
            statusExport = statusExport.replaceAll("%NAME%", output.getName());
            StatusPanel.addStatusLine(statusExport);
        } catch(TranscoderException | IOException e) {
            e.printStackTrace();
        }

        //das PNG-File je nach Auswahl drehen
        try {
            rotateImage(output);

            String statusRotate = Main.config.getLanguageWord("statusRotate");
            StatusPanel.addStatusLine(statusRotate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setStyleColor(Color color) {
        styleColor = color;
    }

    private static void button2Corners() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();
        Node currNode;

        //DER LINKE KREIS
        int diameter = measurements.get("height");

        currNode = doc.getElementsByTagName("circle").item(0);
        NamedNodeMap leftCircleItems = currNode.getAttributes();

        Node left_cx = leftCircleItems.getNamedItem("cx");
        Node left_cy = leftCircleItems.getNamedItem("cy");
        Node left_r  = leftCircleItems.getNamedItem("r");
        Node left_style = leftCircleItems.getNamedItem("style");

        left_r.setTextContent("" + pxValueInMm(diameter / 2));
        left_cx.setTextContent("" + pxValueInMm(diameter / 2));
        left_cy.setTextContent("" + pxValueInMm(diameter / 2));
        left_style.setTextContent(getStyleString());

        //DAS RECHTECK
        int rectX = diameter/2, rectY = 0, rectWidth = measurements.get("width") - diameter, rectHeight = diameter;

        currNode = doc.getElementsByTagName("rect").item(0);
        NamedNodeMap rectItems = currNode.getAttributes();

        Node rect_width = rectItems.getNamedItem("width"),
                rect_height = rectItems.getNamedItem("height"),
                rect_x = rectItems.getNamedItem("x"),
                rect_y = rectItems.getNamedItem("y"),
                rect_style = rectItems.getNamedItem("style");

        rect_width.setTextContent("" + pxValueInMm(rectWidth));
        rect_height.setTextContent("" + pxValueInMm(rectHeight));
        rect_x.setTextContent("" + pxValueInMm(rectX));
        rect_y.setTextContent("" + pxValueInMm(rectY));
        rect_style.setTextContent(getStyleString());

        //DER RECHTE KREIS
        currNode = doc.getElementsByTagName("circle").item(1);
        NamedNodeMap rightCircleItems = currNode.getAttributes();

        Node right_cx = rightCircleItems.getNamedItem("cx");
        Node right_cy = rightCircleItems.getNamedItem("cy");
        Node right_r  = rightCircleItems.getNamedItem("r");
        Node right_style = rightCircleItems.getNamedItem("style");

        right_cx.setTextContent("" + pxValueInMm(rectX + rectWidth));
        right_cy.setTextContent("" + pxValueInMm(diameter/2));
        right_r.setTextContent("" + pxValueInMm(diameter/2));
        right_style.setTextContent(getStyleString());

        //DIE GRÖßE DES DOKUMENTES ANPASSEN
        changeDocumentSize(measurements.get("width"), measurements.get("height"));
    }

    private static void button1Corner() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();
        int width = measurements.get("width"), height = measurements.get("height");

        //Das Rechteck
        Node rect = doc.getElementsByTagName("rect").item(0);
        NamedNodeMap rectItems = rect.getAttributes();

        Node rWidth = rectItems.getNamedItem("width");
        Node rHeight = rectItems.getNamedItem("height");
        Node rStyle = rectItems.getNamedItem("style");

        rWidth.setTextContent("" + pxValueInMm(width - height/2));
        rHeight.setTextContent("" + pxValueInMm(height));
        rStyle.setTextContent(getStyleString());

        //Der Kreis
        Node circle = doc.getElementsByTagName("circle").item(0);
        NamedNodeMap circleItems = circle.getAttributes();

        Node radius = circleItems.getNamedItem("r");
        Node cx = circleItems.getNamedItem("cx");
        Node cy = circleItems.getNamedItem("cy");
        Node cStyle = circleItems.getNamedItem("style");

        radius.setTextContent("" + pxValueInMm(height/2));
        cx.setTextContent("" + pxValueInMm(width - height/2));
        cy.setTextContent("" + pxValueInMm(height/2));
        cStyle.setTextContent(getStyleString());

        //Die Größe des Dokumentes anpassen
        changeDocumentSize(width, height);
    }

    private static void buttonCircle() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();
        int diameter = measurements.get("diameter");
        Node node = doc.getElementsByTagName("circle").item(0);
        NamedNodeMap items = node.getAttributes();

        Node radius = items.getNamedItem("r");
        Node cx = items.getNamedItem("cx");
        Node cy = items.getNamedItem("cy");
        Node style = items.getNamedItem("style");

        radius.setTextContent("" + pxValueInMm(diameter/2));
        cx.setTextContent("" + pxValueInMm(diameter/2));
        cy.setTextContent("" + pxValueInMm(diameter/2));
        style.setTextContent(getStyleString());

        //noch die Größe des Dokuments anpassen
        changeDocumentSize(diameter, diameter);
    }

    private static void buttonCutOff() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();

        //DAS LINKE / KLEINE DREIECK
        Node leftRect = doc.getElementsByTagName("rect").item(1);
        NamedNodeMap leftRectItems = leftRect.getAttributes();
        Node leftRectX = leftRectItems.getNamedItem("x");
        Node leftRectY = leftRectItems.getNamedItem("y");
        Node leftRectW = leftRectItems.getNamedItem("width");
        Node leftRectH = leftRectItems.getNamedItem("height");
        Node leftRectStyle = leftRectItems.getNamedItem("style");

        leftRectX.setTextContent("0");
        leftRectY.setTextContent("0");
        leftRectH.setTextContent("" + pxValueInMm(measurements.get("h")));
        leftRectW.setTextContent("" + pxValueInMm(measurements.get("w1")));
        leftRectStyle.setTextContent(getStyleString());

        //DAS RECHTE / GROßE RECHTECK
        Node rightRect = doc.getElementsByTagName("rect").item(0);
        NamedNodeMap rightRectItems = rightRect.getAttributes();
        Node rightRectX = rightRectItems.getNamedItem("x");
        Node rightRectY = rightRectItems.getNamedItem("y");
        Node rightRectW = rightRectItems.getNamedItem("width");
        Node rightRectH = rightRectItems.getNamedItem("height");
        Node rightRectStyle = rightRectItems.getNamedItem("style");

        rightRectX.setTextContent("0");
        rightRectY.setTextContent("0");
        rightRectH.setTextContent("" + pxValueInMm(measurements.get("h")));
        rightRectW.setTextContent("" + pxValueInMm(measurements.get("w2")));
        rightRectStyle.setTextContent(getStyleString());

        rightRectX.setTextContent("" + pxValueInMm(measurements.get("w1") + measurements.get("d")));
        rightRectY.setTextContent("0");
        rightRectH.setTextContent("" + pxValueInMm(measurements.get("h")));
        rightRectW.setTextContent("" + pxValueInMm(measurements.get("w2") - measurements.get("h")/2));
        rightRectStyle.setTextContent(getStyleString());

        //DER KREIS
        Node circle = doc.getElementsByTagName("circle").item(0);
        NamedNodeMap circleItems = circle.getAttributes();
        Node circleCX = circleItems.getNamedItem("cx");
        Node circleCY = circleItems.getNamedItem("cy");
        Node circleR = circleItems.getNamedItem("r");
        Node circleStyle = circleItems.getNamedItem("style");

        circleCX.setTextContent("" + pxValueInMm(measurements.get("w1") + measurements.get("d") + measurements.get("w2") - measurements.get("h")/2));
        circleCY.setTextContent("" + pxValueInMm(measurements.get("h")/2));
        circleR.setTextContent("" + pxValueInMm(measurements.get("h")/2));
        circleStyle.setTextContent(getStyleString());

        //DIE GRÖßE DES DOKUMENTES ANPASSEN
        changeDocumentSize(measurements.get("w1") + measurements.get("d") + measurements.get("w2"),
                measurements.get("h"));
    }

    private static void buttonRectangle() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();

        Node node = doc.getElementsByTagName("rect").item(0);
        int width = measurements.get("width");
        int height = measurements.get("height");

        NamedNodeMap items = node.getAttributes();

        Node widthNode = items.getNamedItem("width");
        Node heightNode = items.getNamedItem("height");
        Node styleNode = items.getNamedItem("style");

        widthNode.setTextContent("" + pxValueInMm(width));
        heightNode.setTextContent("" + pxValueInMm(height));
        styleNode.setTextContent(getStyleString());

        changeDocumentSize(width, height);
    }

    private static void itemBulge() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();

        //DAS OBERE RECHTECK / HAUPTRECHTECK
        Node mainRec = doc.getElementsByTagName("rect").item(0);
        NamedNodeMap mainRecItems = mainRec.getAttributes();
        Node mainRecX = mainRecItems.getNamedItem("x");
        Node mainRecY = mainRecItems.getNamedItem("y");
        Node mainRecW = mainRecItems.getNamedItem("width");
        Node mainRecH = mainRecItems.getNamedItem("height");
        Node mainRecStyle = mainRecItems.getNamedItem("style");
        mainRecX.setTextContent("0");
        mainRecY.setTextContent("0");
        mainRecW.setTextContent("" + pxValueInMm(measurements.get("w1")));
        mainRecH.setTextContent("" + pxValueInMm(measurements.get("h1")));
        mainRecStyle.setTextContent(getStyleString());

        //DAS UNTERE RECHTECK
        Node secRec = doc.getElementsByTagName("rect").item(1);
        NamedNodeMap secRecItems = secRec.getAttributes();
        Node secRecX = secRecItems.getNamedItem("x");
        Node secRecY = secRecItems.getNamedItem("y");
        Node secRecW = secRecItems.getNamedItem("width");
        Node secRecH = secRecItems.getNamedItem("height");
        Node secRecStyle = secRecItems.getNamedItem("style");
        secRecX.setTextContent("" + pxValueInMm(measurements.get("w2")));
        secRecY.setTextContent("" + pxValueInMm(measurements.get("h1")));
        secRecW.setTextContent("" + pxValueInMm(measurements.get("w3")));
        secRecH.setTextContent("" + pxValueInMm(measurements.get("h2")));
        secRecStyle.setTextContent(getStyleString());

        //DIE LINKE ABRUNDUNG
        Node leftCurve = doc.getElementsByTagName("path").item(1);
        NamedNodeMap lCurveItems = leftCurve.getAttributes();
        Node lcS = lCurveItems.getNamedItem("style");
        Node lcD = lCurveItems.getNamedItem("d");
        lcS.setTextContent(getStyleString());
        lcD.setTextContent("M " + pxValueInMm(measurements.get("w2")) + " " + pxValueInMm(measurements.get("h1") + measurements.get("h2")) + " " +
                "v -" + pxValueInMm(measurements.get("h2")) + " " +
                "h -" + pxValueInMm(measurements.get("h2")) + " " +
                "Q " + pxValueInMm(measurements.get("w2")) + " " + pxValueInMm(measurements.get("h1")) + " " +
                pxValueInMm(measurements.get("w2")) + " " + pxValueInMm(measurements.get("h1") + measurements.get("h2")) + " Z");

        //DIE RECHTE ABRUNDUNG
        Node rightCurve = doc.getElementsByTagName("path").item(0);
        NamedNodeMap rCurveItems = rightCurve.getAttributes();
        Node rcS = rCurveItems.getNamedItem("style");
        Node rcD = rCurveItems.getNamedItem("d");
        rcS.setTextContent(getStyleString());
        rcD.setTextContent("M " + pxValueInMm(measurements.get("w2") + measurements.get("w3")) + " " + pxValueInMm(measurements.get("h1") + measurements.get("h2")) + " " +
                "v -" + pxValueInMm(measurements.get("h2")) + " " +
                "h " + pxValueInMm(measurements.get("h2")) +
                "Q " + pxValueInMm(measurements.get("w2") + measurements.get("w3")) + " " + pxValueInMm(measurements.get("h1")) + " " +
                pxValueInMm(measurements.get("w2") + measurements.get("w3")) + " " + pxValueInMm(measurements.get("h2") + measurements.get("h1")) + " Z");

        changeDocumentSize(measurements.get("w1") + 200, measurements.get("h1") + measurements.get("h2") + 200);
    }

    private static void itemCornerRight() {
        HashMap<String, Integer> measurements = Storage.getMeasurements();
        int r1 = measurements.get("r1");

        //Das obere Rechteck
        Node rightRect = doc.getElementsByTagName("rect").item(0);
        NamedNodeMap rRectItems = rightRect.getAttributes();
        Node rRecX = rRectItems.getNamedItem("x");
        Node rRecY = rRectItems.getNamedItem("y");
        Node rRecW = rRectItems.getNamedItem("width");
        Node rRecH = rRectItems.getNamedItem("height");
        Node rRecS = rRectItems.getNamedItem("style");
        rRecX.setTextContent("" + pxValueInMm(r1));
        rRecY.setTextContent("0");
        rRecW.setTextContent("" + pxValueInMm(measurements.get("w1") - r1));
        rRecH.setTextContent("" + pxValueInMm(measurements.get("h2")));
        rRecS.setTextContent(getStyleString());

        //Das untere linke Rechteck
        Node leftRect = doc.getElementsByTagName("rect").item(1);
        NamedNodeMap lRectItems = leftRect.getAttributes();
        Node lRecX = lRectItems.getNamedItem("x");
        Node lRecY = lRectItems.getNamedItem("y");
        Node lRecW = lRectItems.getNamedItem("width");
        Node lRecH = lRectItems.getNamedItem("height");
        Node lRecS = lRectItems.getNamedItem("style");
        lRecX.setTextContent("0");
        lRecY.setTextContent("" + pxValueInMm(measurements.get("r1")));
        lRecW.setTextContent("" + pxValueInMm(measurements.get("w2")));
        lRecH.setTextContent("" + pxValueInMm(measurements.get("h1") - measurements.get("r1")));
        System.out.println(lRecH.getTextContent());
        lRecS.setTextContent(getStyleString());

        //Den Pfad in der rechten inneren Ecke
        Node path1 = doc.getElementsByTagName("path").item(0);
        NamedNodeMap path1Items = path1.getAttributes();
        Node path1Style = path1Items.getNamedItem("style");
        Node path1D = path1Items.getNamedItem("d");
        path1Style.setTextContent(getStyleString());
        path1D.setTextContent("M " + pxValueInMm(measurements.get("w2") + measurements.get("r2")) + " " + pxValueInMm(measurements.get("h2")) + " " + //der Startpunkt
                "h -" + pxValueInMm(measurements.get("r2")) + " " +
                "v " + pxValueInMm(measurements.get("r2")) + " " +
                "Q " + pxValueInMm(measurements.get("w2")) + " " + pxValueInMm(measurements.get("h2")) + " " +
                pxValueInMm(measurements.get("w2") + measurements.get("r2")) + " " + pxValueInMm(measurements.get("h2")) + " Z");

        //Den Pfad in der linken äußeren Ecke
        Node path2 = doc.getElementsByTagName("path").item(1);
        NamedNodeMap path2Items = path2.getAttributes();
        Node path2Style = path2Items.getNamedItem("style");
        Node path2D = path2Items.getNamedItem("d");
        path2Style.setTextContent(getStyleString());
        path2D.setTextContent("M " + pxValueInMm(measurements.get("r1")) + " 0 " +
                "v " + pxValueInMm(measurements.get("r1")) + " " +
                "h -" + pxValueInMm(measurements.get("r1")) + " " +
                "Q 0 0 " + pxValueInMm(measurements.get("r1")) + " 0" + " Z");

        changeDocumentSize(measurements.get("w1"), measurements.get("h1"));
    }

    private static double pxValueInMm(int px) {
        return px/3.779528;
    }

    private static void storeAsPNG(File svgFile, File output) throws IOException, TranscoderException {
        //Input- und Output-File laden
        String svgURI = Paths.get(svgFile.getAbsolutePath()).toUri().toURL().toString();
//        File output = Storage.getLocation();

        //Transcoder-Input und Transcoder-Output laden
        TranscoderInput transcoderInput = new TranscoderInput(svgURI);
        OutputStream outputStream = new FileOutputStream(output);
        TranscoderOutput transcoderOutput = new TranscoderOutput(outputStream);

        //das PNG-File aus dem SVG-File erstellen
        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.transcode(transcoderInput, transcoderOutput);

        //den Outputstream schließen
        outputStream.flush();
        outputStream.close();
    }

    private static String getStyleString() {
        String hex = String.format("#%02x%02x%02x", styleColor.getRed(), styleColor.getGreen(), styleColor.getBlue());

        //return "fill:" + hex + ";stroke-width:0.326475";
        return "fill:" + hex + ";stroke-width:0";
    }

    private static void changeDocumentSize(int width, int height) {
        Node svgNode = doc.getFirstChild();
        NamedNodeMap svgMap = svgNode.getAttributes();
        Node widthNode = svgMap.getNamedItem("width");
        widthNode.setTextContent("" + width);
        Node heightNode = svgMap.getNamedItem("height");
        heightNode.setTextContent("" + height);
        Node viewBoxNode = svgMap.getNamedItem("viewBox");
        viewBoxNode.setTextContent("0 0 " + pxValueInMm(width) + " " + pxValueInMm(height));
    }

    private static void rotateImage(File imgFile) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);

        int rotation = Storage.getOrientation() - 1;
        double rads = Math.toRadians(rotation * 90);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int oldW = img.getWidth(), oldH = img.getHeight();
        int newW = (int) Math.floor(oldW * cos + oldH * sin);
        int newH = (int) Math.floor(oldH * cos + oldW * sin);

        BufferedImage rotatedImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImg.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newW - oldW) / 2, (newH - oldH) / 2);

        int x = oldW/2, y = oldH/2;
        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        ImageIO.write(rotatedImg, "png", imgFile);
    }
}
