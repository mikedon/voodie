package com.voodie.web;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Voodie
 * User: MikeD
 */
public class CheckInPdf {

    public ByteArrayOutputStream getCheckInPdf(String electionUrl){
        ByteArrayOutputStream output;
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDFont font = PDType1Font.HELVETICA;
            PDPageContentStream contentStream;
            PDJpeg front;
            InputStream inputFront;
            output = new ByteArrayOutputStream();

            //qr code
            inputFront = new FileInputStream(QRCode.from(electionUrl)
                    .to(ImageType.PNG).file());
            BufferedImage buffFront = ImageIO.read(inputFront);
            front = new PDJpeg(document, buffFront);

            //NOTE the order of creating the content stream and any PDJpeg matters:
            //see http://stackoverflow.com/questions/8521290/cant-add-an-image-to-a-pdf-using-pdfbox
            contentStream = new PDPageContentStream(document, page);

            contentStream.drawImage(front,220,530);

            //Check In title
            contentStream.beginText();
            contentStream.setFont(font, 24);
            contentStream.moveTextPositionByAmount(200, 750);
            contentStream.drawString("Voodie Check In");
            contentStream.endText();

            //Check In sub header
            contentStream.beginText();
            contentStream.setFont(font, 14);
            contentStream.moveTextPositionByAmount(150, 700);
            contentStream.drawString("Thanks for using Voodie!  Use a QR Code reader ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 14);
            contentStream.moveTextPositionByAmount(200, 680);
            contentStream.drawString("on your smartphone to check in");
            contentStream.endText();

            //link text
            contentStream.beginText();
            contentStream.setFont(font, 14);
            contentStream.moveTextPositionByAmount(260, 480);
            contentStream.drawString("or visit");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 18);
            contentStream.moveTextPositionByAmount(120, 430);
            contentStream.drawString(electionUrl);
            contentStream.endText();

            //close
            contentStream.close();
            document.save(output);
            document.close();

        }catch(IOException e){
            throw new RuntimeException(e);
        } catch (COSVisitorException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

}
