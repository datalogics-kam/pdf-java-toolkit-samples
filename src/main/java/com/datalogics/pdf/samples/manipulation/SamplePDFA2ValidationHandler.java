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

package com.datalogics.pdf.samples.manipulation;

import com.adobe.internal.xmp.options.PropertyOptions;
import com.adobe.pdfjt.core.types.ASName;
import com.adobe.pdfjt.pdf.document.PDFContents;
import com.adobe.pdfjt.pdf.document.PDFResources;
import com.adobe.pdfjt.pdf.graphics.PDFExtGState;
import com.adobe.pdfjt.pdf.graphics.colorspaces.PDFColorSpace;
import com.adobe.pdfjt.pdf.graphics.font.PDFFont;
import com.adobe.pdfjt.pdf.graphics.patterns.PDFPattern;
import com.adobe.pdfjt.pdf.graphics.xobject.PDFXObject;
import com.adobe.pdfjt.pdf.interactive.annotation.PDFAnnotation;
import com.adobe.pdfjt.pdf.interactive.forms.PDFField;
import com.adobe.pdfjt.pdf.interactive.navigation.PDFBookmark;
import com.adobe.pdfjt.pdf.interchange.prepress.PDFOutputIntent;
import com.adobe.pdfjt.pdf.page.PDFPage;
import com.adobe.pdfjt.services.pdfa2.EmbeddedFilePDFA1ValidationHandler;
import com.adobe.pdfjt.services.pdfa2.EmbeddedFilePDFA2ValidationHandler;
import com.adobe.pdfjt.services.pdfa2.PDFA2InvalidNamespaceUsage;
import com.adobe.pdfjt.services.pdfa2.PDFA2SaveTypes;
import com.adobe.pdfjt.services.pdfa2.PDFA2ValidationHandler;
import com.adobe.pdfjt.services.pdfa2.error.PDFA2ErrorSet;
import com.adobe.pdfjt.services.pdfa2.error.PDFA2XMPErrorCollector;
import com.adobe.pdfjt.services.pdfa2.error.codes.*;
import com.adobe.pdfjt.xml.XMLElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * This is sample implementation of {@link PDFA2ValidationHandler}
 *
 */
class SamplePDFA2ValidationHandler implements PDFA2ValidationHandler {
    private PDFA2SaveTypes saveTypes;

    private int pageIndex = -1;
    private boolean errorsFound = false;

    public boolean errorsFound() {
        return errorsFound;
    }

    public PDFA2SaveTypes getSaveTypes() {
        return saveTypes;
    }

    private void printError(PDFA2AbstractErrorCode error, String uri) {

        if (!errorsFound)
            System.out.println("PDF document is not compliant with PDF/A-2b");
        errorsFound = true;

        if (uri != null) {
            System.out.println("\n\n---> Error corresponding to URI: " + uri);
            if (PDFA2DocumentValidation.ACROBAT_COMPLIANT_MSG)
                System.out.println("\n\t-> " + error.getAcrobatComplaintMessage());
            else
                System.out.println("\n\t-> " + error.toString());
        } else if (PDFA2DocumentValidation.ACROBAT_COMPLIANT_MSG)
            System.out.println("\n\n-> " + error.getAcrobatComplaintMessage());
        else
            System.out.println("\n\n-> " + error.toString());

        int objectNumber = error.getObjectNumber();
        if (objectNumber > 0)
            System.out.println("obj(" + objectNumber + ", " + error.getGenNumber() + ")");
        if (pageIndex > 0)
            System.out.println("Page: " + pageIndex);
    }


    public boolean annotationError(PDFA2ErrorSet<PDFA2AbstractAnnotationErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractAnnotationErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractAnnotationErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error.getSubType() != null)
                    System.out.println("Annotation type: " + error.getSubType());
                if (error instanceof PDFA2AnnotationErrorNonPermittedNamedActionFound) {
                    System.out.println("Named action used: "
                                       + ((PDFA2AnnotationErrorNonPermittedNamedActionFound) error).getName());
                } else if (error instanceof PDFA2AnnotationErrorFlagHiddenSet
                           || error instanceof PDFA2AnnotationErrorFlagInvisibleSet
                           || error instanceof PDFA2AnnotationErrorFlagNoRotateNotSet
                           || error instanceof PDFA2AnnotationErrorFlagNoViewSet
                           || error instanceof PDFA2AnnotationErrorFlagNoZoomNotSet
                           || error instanceof PDFA2AnnotationErrorFlagPrintNotSet
                           || error instanceof PDFA2AnnotationErrorFlagToggleNoViewSet) {
                    System.out.println("Flag value: " + error.getFlags());
                }
            }
        }
        return true;
    }

    public boolean beginAnnotationScan(PDFAnnotation annot) {
        return true;
    }

    public boolean beginAnnotationsScan() {
        return true;
    }

    public boolean beginColorSpaceScan(ASName name, PDFColorSpace colorSpace) {
        return true;
    }

    public boolean beginContentScan(PDFContents contents, PDFResources resources) {
        return true;
    }

    public boolean beginDocMetadataScan() {
        return true;
    }

    public boolean beginDocumentScan() {
        return true;
    }

    public boolean beginFontScan(ASName name, PDFFont font) {
        return true;
    }

    public boolean beginFileStructureScan() {
        return true;
    }

    public boolean beginFormFieldScan(PDFField field) {
        return true;
    }

    public boolean beginFormFieldTreeScan() {
        return true;
    }

    public boolean beginOutputIntentScan() {
        return true;
    }

    public boolean beginPageScan(PDFPage page, int index) {
        this.pageIndex = index;
        return true;
    }

    public boolean beginPageTreeScan() {
        return true;
    }

    public boolean beginPatternScan(ASName name, PDFPattern pattern) {
        return true;
    }

    public boolean beginXObjectScan(ASName name, PDFXObject xObject) {
        return true;
    }

    public boolean bookmarkError(PDFBookmark bookmark, PDFA2ErrorSet<PDFA2AbstractBookmarkErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractBookmarkErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractBookmarkErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2BookmarkErrorNonPermittedNamedActionFound) {
                    System.out.println("Named action used: "
                                       + ((PDFA2BookmarkErrorNonPermittedNamedActionFound) error).getName());
                }
            }
        }
        return true;
    }

    public boolean catalogError(PDFA2ErrorSet<PDFA2AbstractCatalogErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractCatalogErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractCatalogErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2CatalogErrorNonPermittedNamedActionFound) {
                    System.out.println("Named action used: "
                                       + ((PDFA2CatalogErrorNonPermittedNamedActionFound) error).getName());
                } else if (error instanceof PDFA2CatalogErrorNamedEmbeddedFilesAreNotValidPDFAFile) {
                    System.out.println("Embedded file name: "
                                       + ((PDFA2CatalogErrorNamedEmbeddedFilesAreNotValidPDFAFile) error).getFileName());
                } else if (error instanceof PDFA2CatalogErrorStructureTypeNameNotValidUTF8) {
                    System.out.println("Structure Type Name: "
                                       + ((PDFA2CatalogErrorStructureTypeNameNotValidUTF8) error).getStructureTypeName());
                }
            }
        }
        return true;
    }

    public boolean colorSpaceError(PDFA2ErrorSet<PDFA2AbstractColorSpaceErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractColorSpaceErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractColorSpaceErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2ColorSpaceErrorDeviceNHasMoreThan32Colourants) {
                    System.out.println("Number of colorants: "
                                       + ((PDFA2ColorSpaceErrorDeviceNHasMoreThan32Colourants) error).getNumberOfColorants());
                } else if (error instanceof PDFA2ColorSpaceErrorICCProfileVersion5OrNewer) {
                    System.out.println("Profile version: "
                                       + ((PDFA2ColorSpaceErrorICCProfileVersion5OrNewer) error).getProfileVersion());
                } else if (error instanceof PDFA2ColorSpaceErrorICCProfileVersionOlderThan2) {
                    System.out.println("Profile version: "
                                       + ((PDFA2ColorSpaceErrorICCProfileVersionOlderThan2) error).getProfileVersion());
                } else if (error instanceof PDFA2ColorSpaceErrorColorantNameNotValidUTF8) {
                    System.out.println("Colorant Name: "
                                       + ((PDFA2ColorSpaceErrorColorantNameNotValidUTF8) error).getColorantName());
                } else if (error instanceof PDFA2ColorSpaceErrorNotDeviceIndependent) {
                    System.out.println("Color Space: "
                                       + ((PDFA2ColorSpaceErrorNotDeviceIndependent) error).getBaseColorSpaceName());
                } else if (error instanceof PDFA2ColorSpaceErrorAlternateCSNotDeviceIndependent) {
                    System.out.println("Color Space: "
                                       + ((PDFA2ColorSpaceErrorAlternateCSNotDeviceIndependent) error).getBaseColorSpaceName());
                }
            }
        }
        return true;
    }

    public boolean contentError(ASName operatorName, PDFA2ErrorSet<PDFA2AbstractContentStreamErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractContentStreamErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractContentStreamErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2ContentStreamErrorIntegerNumberValueTooHigh) {
                    System.out.println("Integer Value: "
                                       + ((PDFA2ContentStreamErrorIntegerNumberValueTooHigh) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorIntegerNumberValueTooLow) {
                    System.out.println("Integer Value: "
                                       + ((PDFA2ContentStreamErrorIntegerNumberValueTooLow) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorRealNumberValueTooHigh) {
                    System.out.println("Real Value: "
                                       + ((PDFA2ContentStreamErrorRealNumberValueTooHigh) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorRealNumberValueTooLow) {
                    System.out.println("Real Value: "
                                       + ((PDFA2ContentStreamErrorRealNumberValueTooLow) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorPositiveValueTooCloseToZero) {
                    System.out.println("Real Value: "
                                       + ((PDFA2ContentStreamErrorPositiveValueTooCloseToZero) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorNegativeValueTooCloseToZero) {
                    System.out.println("Real Value: "
                                       + ((PDFA2ContentStreamErrorNegativeValueTooCloseToZero) error).getNumber());
                } else if (error instanceof PDFA2ContentStreamErrorNameLengthIncorrect) {
                    System.out.println("Name Used: " + ((PDFA2ContentStreamErrorNameLengthIncorrect) error).getName());
                } else if (error instanceof PDFA2ContentStreamErrorStringLengthIncorrect) {
                    System.out.println("String Used: "
                                       + ((PDFA2ContentStreamErrorStringLengthIncorrect) error).getString());
                } else if (error instanceof PDFA2ContentStreamErrorMoreThan28NestingLevelsUsed) {
                    System.out.println("Nested Levels: "
                                       + ((PDFA2ContentStreamErrorMoreThan28NestingLevelsUsed) error).getNestedLevels());
                } else if (error instanceof PDFA2ContentStreamErrorUnknownOperator) {
                    System.out.println("Unknown operator: "
                                       + ((PDFA2ContentStreamErrorUnknownOperator) error).getOperator());
                } else if (error instanceof PDFA2ContentStreamErrorInvalidRenderingIntent) {
                    System.out.println("Rendering Intent: "
                                       + ((PDFA2ContentStreamErrorInvalidRenderingIntent) error).getRenderingIntent());
                } else if (error instanceof PDFA2ContentStreamErrorUsesDeviceDependentColorSpace) {
                    System.out.println("Color Space: "
                                       + ((PDFA2ContentStreamErrorUsesDeviceDependentColorSpace) error).getBaseColorSpaceName());
                } else if (error instanceof PDFA2ContentStreamErrorInlineImageUsesNonStandardFilter) {
                    System.out.println("Filter Name: "
                                       + ((PDFA2ContentStreamErrorInlineImageUsesNonStandardFilter) error).getFilterName());
                }
            }
        }
        return true;
    }

    public boolean docMetadataError(PDFA2ErrorSet<PDFA2AbstractDocumentMetadataErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractDocumentMetadataErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractDocumentMetadataErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2DocumentMetadataErrorPDFAVersionScanMismatch) {
                    System.out.println("Version: "
                                       + ((PDFA2DocumentMetadataErrorPDFAVersionScanMismatch) error).getVersion());
                } else if (error instanceof PDFA2DocumentMetadataErrorPDFAConformanceScanMismatch) {
                    System.out.println("Conformance level: "
                                       + ((PDFA2DocumentMetadataErrorPDFAConformanceScanMismatch) error).getComformanceLevel());
                }
            }
        }
        return true;
    }

    public boolean endAnnotationScan() {
        return true;
    }

    public boolean endAnnotationsScan() {
        return true;
    }

    public boolean endColorSpaceScan() {
        return true;
    }

    public boolean endContentScan() {
        return true;
    }

    public boolean endDocMetadataScan() {
        return true;
    }

    public boolean endDocumentScan(boolean errorsFound) {
        return true;
    }

    public boolean endFontScan() {
        return true;
    }

    public boolean endFileStructureScan(PDFA2SaveTypes saveTypes) {
        this.saveTypes = saveTypes;
        return true;
    }

    public boolean endFormFieldScan() {
        return true;
    }

    public boolean endFormFieldTreeScan() {
        return true;
    }

    public boolean endOutputIntentScan() {
        return true;
    }

    public boolean endPageScan() {
        pageIndex = -1;
        return true;
    }

    public boolean endPageTreeScan() {
        return true;
    }

    public boolean endPatternScan() {
        return true;
    }

    public boolean endXObjectScan() {
        return true;
    }

    public boolean extGStateError(PDFExtGState extGState, PDFA2ErrorSet<PDFA2AbstractExtGStateErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractExtGStateErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractExtGStateErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2ExtGStateErrorTransferFunction2ValueNotAllowed) {
                    System.out.println("TR2 value: "
                                       + ((PDFA2ExtGStateErrorTransferFunction2ValueNotAllowed) error).getValue());
                } else if (error instanceof PDFA2ExtGStateErrorHalfToneTypeNotAllowed) {
                    System.out.println("HalfTone type: "
                                       + ((PDFA2ExtGStateErrorHalfToneTypeNotAllowed) error).getHalfToneType());
                } else if (error instanceof PDFA2ExtGStateErrorInvalidRenderingIntent) {
                    System.out.println("Rendering intent: "
                                       + ((PDFA2ExtGStateErrorInvalidRenderingIntent) error).getRenderingIntent());
                } else if (error instanceof PDFA2ExtGStateErrorInvalidBlendMode) {
                    System.out.println("Blend mode: " + ((PDFA2ExtGStateErrorInvalidBlendMode) error).getBlendMode());
                }
            }
        }
        return true;
    }

    public boolean fileStructureError(PDFA2ErrorSet<PDFA2AbstractFileStructureErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractFileStructureErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractFileStructureErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2FileStructureErrorHeaderVersionInvalid) {
                    System.out.println("Header Version: "
                                       + ((PDFA2FileStructureErrorHeaderVersionInvalid) error).getHeaderVersion());
                } else if (error instanceof PDFA2FileStructureErrorHexStrEvenNumCharsNotPresent) {
                    System.out.println("Hex String: "
                                       + ((PDFA2FileStructureErrorHexStrEvenNumCharsNotPresent) error).getHexString());
                } else if (error instanceof PDFA2FileStructureErrorStreamDLKeyInvalid) {
                    System.out.println("Actual Decoded Bytes: "
                                       + ((PDFA2FileStructureErrorStreamDLKeyInvalid) error).getActualDecodedStreamBytes());
                    System.out.println("DL key value: "
                                       + ((PDFA2FileStructureErrorStreamDLKeyInvalid) error).getStreamDLValuePresentinDict());
                } else if (error instanceof PDFA2FileStructureErrorIntegerNumberValueTooHigh) {
                    System.out.println("Integer Value: "
                                       + ((PDFA2FileStructureErrorIntegerNumberValueTooHigh) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorIntegerNumberValueTooLow) {
                    System.out.println("Integer Value: "
                                       + ((PDFA2FileStructureErrorIntegerNumberValueTooLow) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorRealNumberValueTooHigh) {
                    System.out.println("Real Value: "
                                       + ((PDFA2FileStructureErrorRealNumberValueTooHigh) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorRealNumberValueTooLow) {
                    System.out.println("Real Value: "
                                       + ((PDFA2FileStructureErrorRealNumberValueTooLow) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorPositiveValueTooCloseToZero) {
                    System.out.println("Real Value: "
                                       + ((PDFA2FileStructureErrorPositiveValueTooCloseToZero) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorNegativeValueTooCloseToZero) {
                    System.out.println("Real Value: "
                                       + ((PDFA2FileStructureErrorNegativeValueTooCloseToZero) error).getNumber());
                } else if (error instanceof PDFA2FileStructureErrorNameLengthIncorrect) {
                    System.out.println("Name Used: " + ((PDFA2FileStructureErrorNameLengthIncorrect) error).getName());
                } else if (error instanceof PDFA2FileStructureErrorStringLengthIncorrect) {
                    System.out.println("String Used: "
                                       + ((PDFA2FileStructureErrorStringLengthIncorrect) error).getString());
                } else if (error instanceof PDFA2FileStructureErrorCryptDecodeFilterPresentWithoutIdentity) {
                    System.out.println("Crypt filter name: "
                                       + ((PDFA2FileStructureErrorCryptDecodeFilterPresentWithoutIdentity) error).getName());
                } else if (error instanceof PDFA2FileStructureErrorNonStandardFilterUsed) {
                    System.out.println("Filter name: "
                                       + ((PDFA2FileStructureErrorNonStandardFilterUsed) error).getFilterName());
                } else if (error instanceof PDFA2FileStructureErrorIndirectObjectNumberIncorrect) {
                    System.out.println("Number of Indirect Objects in document: "
                                       + ((PDFA2FileStructureErrorIndirectObjectNumberIncorrect) error).getNumberOfIndirectObjects());
                }
            }
        }
        return true;
    }

    public boolean fontError(PDFA2ErrorSet<PDFA2AbstractFontErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractFontErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractFontErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2FontErrorNameNotUTF8) {
                    System.out.println("Font Name: " + ((PDFA2FontErrorNameNotUTF8) error).getFontName());
                } else if (error instanceof PDFA2FontErrorTypeIncorrectInCMAP) {
                    System.out.println("Type: " + ((PDFA2FontErrorTypeIncorrectInCMAP) error).getTypeValue());
                } else if (error instanceof PDFA2FontErrorTypeIncorrectInFont) {
                    System.out.println("Type: " + ((PDFA2FontErrorTypeIncorrectInFont) error).getTypeValue());
                } else if (error instanceof PDFA2FontErrorTypeIncorrectInCIDFont) {
                    System.out.println("Type: " + ((PDFA2FontErrorTypeIncorrectInCIDFont) error).getTypeValue());
                } else if (error instanceof PDFA2FontErrorTypeIncorrectInFontDescriptor) {
                    System.out.println("Type: "
                                       + ((PDFA2FontErrorTypeIncorrectInFontDescriptor) error).getTypeValue());
                }
            }
        }
        return true;
    }

    public boolean formFieldError(PDFA2ErrorSet<PDFA2AbstractFieldErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractFieldErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractFieldErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
            }
        }
        return true;
    }

    public boolean outputIntentsError(PDFOutputIntent intent,
                                      PDFA2ErrorSet<PDFA2AbstractOutputIntentErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractOutputIntentErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractOutputIntentErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2OutputIntentErrorMultipleDestOutputProfiles) {
                    System.out.println("Profiles used: "
                                       + ((PDFA2OutputIntentErrorMultipleDestOutputProfiles) error).getNumberOfoutputIntentEntries());
                } else if (error instanceof PDFA2OutputIntentErrorICCProfileVersion5OrNewer) {
                    System.out.println("Profile version: "
                                       + ((PDFA2OutputIntentErrorICCProfileVersion5OrNewer) error).getProfileVersion());
                } else if (error instanceof PDFA2OutputIntentErrorICCProfileVersionOlderThan2) {
                    System.out.println("Profile version: "
                                       + ((PDFA2OutputIntentErrorICCProfileVersionOlderThan2) error).getProfileVersion());
                }
            }
        }
        return true;
    }

    public boolean pageError(PDFA2ErrorSet<PDFA2AbstractPageErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractPageErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractPageErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2PageErrorArtBoxHasHeightGreaterThan14400) {
                    System.out.println("Art box height: "
                                       + ((PDFA2PageErrorArtBoxHasHeightGreaterThan14400) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorArtBoxHasHeightLessThan3) {
                    System.out.println("Art box height: "
                                       + ((PDFA2PageErrorArtBoxHasHeightLessThan3) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorArtBoxHasWidthGreaterThan14400) {
                    System.out.println("Art box width: "
                                       + ((PDFA2PageErrorArtBoxHasWidthGreaterThan14400) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorArtBoxHasWidthLessThan3) {
                    System.out.println("Art box width: "
                                       + ((PDFA2PageErrorArtBoxHasWidthLessThan3) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorBleedBoxHasHeightGreaterThan14400) {
                    System.out.println("Bleed box height: "
                                       + ((PDFA2PageErrorBleedBoxHasHeightGreaterThan14400) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorBleedBoxHasHeightLessThan3) {
                    System.out.println("Bleed box height: "
                                       + ((PDFA2PageErrorBleedBoxHasHeightLessThan3) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorBleedBoxHasWidthGreaterThan14400) {
                    System.out.println("Bleed box width: "
                                       + ((PDFA2PageErrorBleedBoxHasWidthGreaterThan14400) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorBleedBoxHasWidthLessThan3) {
                    System.out.println("Bleed box width: "
                                       + ((PDFA2PageErrorBleedBoxHasWidthLessThan3) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorCropBoxHasHeightGreaterThan14400) {
                    System.out.println("Crop box height: "
                                       + ((PDFA2PageErrorCropBoxHasHeightGreaterThan14400) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorCropBoxHasHeightLessThan3) {
                    System.out.println("Crop box height: "
                                       + ((PDFA2PageErrorCropBoxHasHeightLessThan3) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorCropBoxHasWidthGreaterThan14400) {
                    System.out.println("Crop box width: "
                                       + ((PDFA2PageErrorCropBoxHasWidthGreaterThan14400) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorCropBoxHasWidthLessThan3) {
                    System.out.println("Crop box width: "
                                       + ((PDFA2PageErrorCropBoxHasWidthLessThan3) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorMediaBoxHasHeightGreaterThan14400) {
                    System.out.println("Media box height: "
                                       + ((PDFA2PageErrorMediaBoxHasHeightGreaterThan14400) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorMediaBoxHasHeightLessThan3) {
                    System.out.println("Media box height: "
                                       + ((PDFA2PageErrorMediaBoxHasHeightLessThan3) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorMediaBoxHasWidthGreaterThan14400) {
                    System.out.println("Media box width: "
                                       + ((PDFA2PageErrorMediaBoxHasWidthGreaterThan14400) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorMediaBoxHasWidthLessThan3) {
                    System.out.println("Media box width: "
                                       + ((PDFA2PageErrorMediaBoxHasWidthLessThan3) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorTrimBoxHasHeightGreaterThan14400) {
                    System.out.println("Trim box height: "
                                       + ((PDFA2PageErrorTrimBoxHasHeightGreaterThan14400) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorTrimBoxHasHeightLessThan3) {
                    System.out.println("Trim box height: "
                                       + ((PDFA2PageErrorTrimBoxHasHeightLessThan3) error).getBoxHeight());
                } else if (error instanceof PDFA2PageErrorTrimBoxHasWidthGreaterThan14400) {
                    System.out.println("Trim box width: "
                                       + ((PDFA2PageErrorTrimBoxHasWidthGreaterThan14400) error).getBoxWidth());
                } else if (error instanceof PDFA2PageErrorTrimBoxHasWidthLessThan3) {
                    System.out.println("Trim box width: "
                                       + ((PDFA2PageErrorTrimBoxHasWidthLessThan3) error).getBoxWidth());
                }
            }
        }
        return true;
    }

    public boolean patternError(PDFA2ErrorSet<PDFA2AbstractPatternErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractPatternErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractPatternErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
            }
        }
        return true;
    }

    public boolean xObjectError(PDFA2ErrorSet<PDFA2AbstractXObjectErrorCode> errors) {
        if (errors != null) {
            Iterator<PDFA2AbstractXObjectErrorCode> itr = errors.getErrorCodes().iterator();
            PDFA2AbstractXObjectErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
                if (error instanceof PDFA2XObjectErrorInvalidRenderingIntent) {
                    System.out.println("Rendering intent: "
                                       + ((PDFA2XObjectErrorInvalidRenderingIntent) error).getRenderingIntent());
                } else if (error instanceof PDFA2XObjectErrorDeviceDependentColorUsed) {
                    System.out.println("Color Space: "
                                       + ((PDFA2XObjectErrorDeviceDependentColorUsed) error).getColorSpaceType());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000HasInvalidNumberOfColourChannels) {
                    System.out.println("Color channels: "
                                       + ((PDFA2XObjectErrorJpeg2000HasInvalidNumberOfColourChannels) error).getNumberOfColorChannels());
                } else if (error instanceof PDFA2XObjectErrorICCProfileVersion5OrNewer) {
                    System.out.println("Profile version: "
                                       + ((PDFA2XObjectErrorICCProfileVersion5OrNewer) error).getProfileVersion());
                } else if (error instanceof PDFA2XObjectErrorICCProfileVersionOlderThan2) {
                    System.out.println("Profile version: "
                                       + ((PDFA2XObjectErrorICCProfileVersionOlderThan2) error).getProfileVersion());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000HasInvalidColourSpecificationMethod) {
                    System.out.println("Method: "
                                       + ((PDFA2XObjectErrorJpeg2000HasInvalidColourSpecificationMethod) error).getMethod());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000UsesInvalidEnumeratedColourSpace) {
                    System.out.println("Color Space: "
                                       + ((PDFA2XObjectErrorJpeg2000UsesInvalidEnumeratedColourSpace) error).getEnumeratedColorSpace());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000ImageUsesBitDepthBelow1) {
                    System.out.println("Bit depth: "
                                       + ((PDFA2XObjectErrorJpeg2000ImageUsesBitDepthBelow1) error).getBitDepthValue());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000ImageUsesBitDepthGreaterThan38) {
                    System.out.println("Bit depth: "
                                       + ((PDFA2XObjectErrorJpeg2000ImageUsesBitDepthGreaterThan38) error).getBitDepthValue());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000UsesDeviceDependentColourSpace) {
                    System.out.println("Color Space: "
                                       + ((PDFA2XObjectErrorJpeg2000UsesDeviceDependentColourSpace) error).getBaseColorSpaceName());
                } else if (error instanceof PDFA2XObjectErrorJpeg2000HasNumberofColorChannelsInconsistentWithCSFromImageDict) {
                    System.out.println("Color Space present in image dictionary: "
                                       + ((PDFA2XObjectErrorJpeg2000HasNumberofColorChannelsInconsistentWithCSFromImageDict) error).getCsFromImageDict());
                    System.out.println("Color channels: "
                                       + ((PDFA2XObjectErrorJpeg2000HasNumberofColorChannelsInconsistentWithCSFromImageDict) error).getNumberOfColorChannels());
                }
            }
        }
        return true;
    }

    public boolean invalidNamespaceUsage(Collection<PDFA2InvalidNamespaceUsage> invalidUsage) {
        return true;
    }

    public boolean invalidTypeUsage(Map<XMLElement, PropertyOptions> invalidTypes) {
        return true;
    }

    public EmbeddedFilePDFA1ValidationHandler getEmbeddedFilePDFA1ValidationHandler() {
        return new EmbeddedFilePDFA1ValidationHandlerSample();
    }

    public EmbeddedFilePDFA2ValidationHandler getEmbeddedFilePDFA2ValidationHandler() {
        return new EmbeddedFilePDFA2ValidationHandlerSample();
    }

    public boolean fontXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    public boolean pageXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    public boolean docXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    public boolean type1FormXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    public boolean iccProfileXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    public boolean imageXMPError(PDFA2XMPErrorCollector errors) {
        printXMPError(errors);
        return true;
    }

    private void printXMPError(PDFA2XMPErrorCollector errors) {

        if (errors != null) {
            Set<Entry<String, PDFA2ErrorSet<PDFA2AbstractXMPErrorCode>>> set = errors.getErrorMap().entrySet();
            Iterator<Entry<String, PDFA2ErrorSet<PDFA2AbstractXMPErrorCode>>> iter = set.iterator();
            PDFA2ErrorSet<PDFA2AbstractXMPErrorCode> errorSet = null;
            while (iter.hasNext()) {
                Entry<String, PDFA2ErrorSet<PDFA2AbstractXMPErrorCode>> entry = iter.next();
                errorSet = entry.getValue();
                Iterator<PDFA2AbstractXMPErrorCode> itr = errorSet.getErrorCodes().iterator();
                PDFA2AbstractXMPErrorCode error = null;
                while (itr.hasNext()) {
                    error = itr.next();
                    printError(error, entry.getKey());
                }
            }

            Iterator<PDFA2AbstractXMPErrorCode> itr = errors.getErrorSet().getErrorCodes().iterator();
            PDFA2AbstractXMPErrorCode error = null;
            while (itr.hasNext()) {
                error = itr.next();
                printError(error, null);
            }
        }
    }

}

