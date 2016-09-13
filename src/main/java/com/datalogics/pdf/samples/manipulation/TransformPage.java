/*
 * ****************************************************************************
 *
 * Copyright 2009-2012 Adobe Systems Incorporated. All Rights Reserved. Portions Copyright 2012-2014 Datalogics
 * Incorporated.
 *
 * NOTICE: Datalogics and Adobe permit you to use, modify, and distribute this file in accordance with the terms of the
 * license agreement accompanying it. If you have received this file from a source other than Adobe or Datalogics, then
 * your use, modification, or distribution of it requires the prior written permission of Adobe or Datalogics.
 *
 * ***************************************************************************
 */

// package pdfjt.core.document;

package com.datalogics.pdf.samples.manipulation;

import com.adobe.pdfjt.core.exceptions.PDFIOException;
import com.adobe.pdfjt.core.exceptions.PDFInvalidDocumentException;
import com.adobe.pdfjt.core.exceptions.PDFInvalidParameterException;
import com.adobe.pdfjt.core.exceptions.PDFSecurityException;
import com.adobe.pdfjt.core.license.LicenseManager;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.pdf.graphics.PDFRectangle;
import com.adobe.pdfjt.pdf.graphics.PDFRotation;
import com.adobe.pdfjt.pdf.page.PDFPage;
import com.adobe.pdfjt.pdf.page.PDFPageTree;
import com.adobe.pdfjt.services.manipulations.PMMPageTransformations;
import com.adobe.pdfjt.services.rasterizer.PageRasterizer;
import com.adobe.pdfjt.services.rasterizer.RasterizationOptions;

import com.datalogics.pdf.document.FontSetLoader;
import com.datalogics.pdf.samples.util.DocumentUtils; // seu
import com.datalogics.pdf.samples.util.IoUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;


/**
 * This sample shows how to convert the pages in a PDF document to landscape letter sized paper (8.5 x 11 inches).
 *
 */
public class TransformPage {
    // private static final String DEFAULT_FILE_PATH = "input/ElementPeriodicTable.pdf";
    // private static final String DEFAULT_FILE_OUT_PATH = ""; // seu -- was "output"
    // private static final String SampleFileServices = null;

    public static final String DEFAULT_INPUT = "ElementPeriodicTable.pdf";


    public static void main(final String[] args) throws Exception {
        // If you are using an evaluation version of the product (License Managed, or LM), set the path to where PDFJT
        // can find the license file.
        //
        // If you are not using an evaluation version of the product you can ignore or remove this code.
        LicenseManager.setLicensePath(".");

        // final PDFDocument pdfDoc = null;
        // final File inputPDFFile = null;
        URL inputUrl = null;


        if (args.length > 0) {
            inputUrl = IoUtils.createUrlFromPath(args[0]);
        } else {
            inputUrl = TransformPage.class.getResource(DEFAULT_INPUT);
        }
        // Get a PDFDocument from an InputStream

        final PDFDocument pdfDoc = DocumentUtils.openPdfDocument(inputUrl);


        final PDFPageTree pages = pdfDoc.requirePages();
        int suffix = 0;
        // Scale all the pages in the given input PDF file.
        final Iterator<PDFPage> pageIter = pages.iterator();
        if (pageIter.hasNext()) {
            final PDFPage page = pageIter.next();
            for (int i = 0; i < 4; i++) {
                rotateRight(pdfDoc, page);
                renderPage(page, ++suffix); // ++suffix
            }
            for (int i = 0; i < 4; i++) {
                rotateLeft(pdfDoc, page);
                renderPage(page, ++suffix); // ++suffix
            }
        }

    }



    private static void rotateLeft(final PDFDocument pdfDoc, final PDFPage curPage)
                    throws PDFInvalidDocumentException, PDFIOException, PDFSecurityException,
                    PDFInvalidParameterException {
        final PMMPageTransformations pageTransformations = new PMMPageTransformations();
        final PDFRectangle mediaBox = curPage.getMediaBox();
        pageTransformations.rotate(PDFRotation.ROTATE_270);
        pageTransformations.translate(0, mediaBox.width());
        pageTransformations.transform(curPage);

        final PDFRectangle newMediaBox = PDFRectangle.newInstance(pdfDoc, 0, 0, mediaBox.height(), mediaBox.width());
        curPage.setMediaBox(newMediaBox);
    }

    private static void rotateRight(final PDFDocument pdfDoc, final PDFPage curPage)
                    throws PDFInvalidDocumentException, PDFIOException, PDFSecurityException,
                    PDFInvalidParameterException {
        final PMMPageTransformations pageTransformations = new PMMPageTransformations();
        final PDFRectangle mediaBox = curPage.getMediaBox();
        pageTransformations.rotate(PDFRotation.ROTATE_90);
        pageTransformations.translate(mediaBox.height(), 0);
        pageTransformations.transform(curPage);

        final PDFRectangle newMediaBox = PDFRectangle.newInstance(pdfDoc, 0, 0, mediaBox.height(), mediaBox.width());
        curPage.setMediaBox(newMediaBox);
    }

    private static final void renderPage(final PDFPage page, final int suffix) throws Exception {

        final FontSetLoader fontSetLoader = FontSetLoader.newInstance();

        final RasterizationOptions rasterizationOptions = new RasterizationOptions();
        // seu rasterizationOptions.setFontSet(SampleFontLoaderUtil.loadSampleFontSet());
        rasterizationOptions.setFontSet(fontSetLoader.getFontSet());

        final PageRasterizer rasterizer = new PageRasterizer(page, rasterizationOptions);
        if (rasterizer.hasNext()) {
            final BufferedImage pageImg = rasterizer.next();
            // ImageIO.write(pageImg, "png", new File(DEFAULT_FILE_OUT_PATH + "/rotated_pdf_" + suffix + ".png"));
            ImageIO.write(pageImg, "png", new File("rotated_pdf_" + suffix + ".png"));
        }
    }
}