package com.webservicex.genericBarCode.operations;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.testng.Reporter;

import com.orasi.exception.AutomationException;
import com.orasi.utils.Base64Coder;
import com.orasi.utils.Constants;
import com.orasi.utils.Randomness;
import com.orasi.utils.TestReporter;
import com.orasi.utils.XMLTools;
import com.webservicex.genericBarCode.GenericBarCode;

public class GenerateBarCode extends GenericBarCode{
    public GenerateBarCode(){
	//Generate a request from a project xml file
	File xml = new File(this.getClass().getResource(Constants.XML_FILES + "/generateBarCode/BarCode_Template.xml").getPath());
	setOperationName("GenerateBarCode");
	setRequestDocument(XMLTools.makeXMLDocument(xml));
	removeComments() ;
	removeWhiteSpace();
    }

    public void setHeight(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Height", value);
    }

    public void setWidth(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Width", value);
    }

    public void setAngle(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Angle", value);
    }

    public void setRatio(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Ratio", value);
    }

    public void setModule(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Module", value);
    }

    public void setLeft(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/Left", value);
    }

    public void setCheckSum(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/CheckSum", value);
    }

    public void setFontName(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/FontName", value);
    }

    public void setBarColor(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/BarColor", value);
    }

    public void setBGColor(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/BGColor", value);
    }

    public void setFontSize(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/FontSize", value);
    }

    public void setBarcodeOption(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/barcodeOption", value);
    }

    public void setBarcodeType(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/barcodeType", value);
    }

    public void setCheckSumMethod(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/checkSumMethod", value);
    }

    public void setShowTextPosition(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/showTextPosition", value);
    }

    public void setBarCodeImageFormat(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeParam/BarCodeImageFormat", value);
    }

    public void setBarCodeText(String value){
	setRequestNodeValueByXPath("/Envelope/Body/GenerateBarCode/BarCodeText", value);
    }
    
    
    public String getBarCodeBytes(){
	return getResponseNodeValueByXPath("/Envelope/Body/GenerateBarCodeResponse/GenerateBarCodeResult");
    }

    public void generateBarCodeImage(){
	try {
	    TestReporter.logDebug("Start creating Barcode image");
	    File tempFolder = new File("tmp");
	    if( !tempFolder.exists()){
		TestReporter.logDebug("Creating tmp directory");
		tempFolder.mkdir();
	    }

	    String uploadFile = "tmp/"+Randomness.randomAlphaNumeric(20)+"_test.png";
	    TestReporter.logDebug("File to create is [ " + uploadFile + " ]");	    
	    
	    BufferedImage image = ImageIO.read(new ByteArrayInputStream(Base64Coder.decode(getBarCodeBytes())));
	    if (image == null) {
		throw new AutomationException("Buffered Image is null");
	    }

	    TestReporter.logDebug("Converted Base64 bytes to image file");
	    File file = new File(uploadFile);
	    ImageIO.write(image, "png", file);

	    TestReporter.logDebug("Saved PNG file to [ " + uploadFile + " ]");
	    Reporter.log("<a href='" + file.getAbsolutePath() + "'> <img src='file:///" + file.getAbsolutePath() + "'/> </a> <br/><br/>");
	    
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	    TestReporter.logDebug("Finished creating image file");
    }
}
