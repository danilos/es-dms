package test.github.richardwilly98.esdms.services;

import static org.elasticsearch.common.io.Streams.copyToByteArray;

import java.util.concurrent.TimeUnit;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import test.github.richardwilly98.esdms.inject.ProviderModule2;

import com.github.richardwilly98.esdms.DocumentImpl;
import com.github.richardwilly98.esdms.FileImpl;
import com.github.richardwilly98.esdms.api.Document;
import com.github.richardwilly98.esdms.api.File;
import com.google.common.base.Stopwatch;

//@Test(enabled = false)
//@Guice(modules = ProviderModule2.class)
public class BulkImportDocumentsTest extends ProviderTestBase {

//	private static final MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
//	private int count = 0;
//
//	private String createDocument(String name, String contentType, String path)
//			throws Throwable {
//		String id = String.valueOf(System.currentTimeMillis());
//		byte[] content = copyToByteArray(new java.io.File(path));
//		File file = new FileImpl.Builder().content(content).name(name)
//				.contentType(contentType).build();
//		Document document = new DocumentImpl.Builder().file(file).id(id)
//				.name(name).roles(null).build();
//		Document newDocument = documentService.create(document);
//		Assert.assertNotNull(newDocument);
//		Assert.assertEquals(id, newDocument.getId());
//		log.info(String.format("New document created #%s", newDocument.getId()));
//		return id;
//	}
//
//	@Test(enabled = false)
//	public void testCreateDocument() throws Throwable {
//		log.info("Start testCreateDocument");
//		loginAdminUser();
//		// createDocument("AIIM Whitepaper - Beyond SharePoint.pdf",
//		// "application/pdf",
//		// "D:/Users/Richard/Documents/OpenText/AIIM Whitepaper - Beyond SharePoint.pdf",
//		// "Aliquam");
//
//		log.info("listFilesAndFilesSubDirectories");
//		Stopwatch stopwatch = new Stopwatch();
//		stopwatch.start();
//		listFilesAndFilesSubDirectories("D:/Users/Richard/Documents/OpenText");
//		stopwatch.stop();
//		log.info(String.format("Duration %s - number of document indexed %s",
//				stopwatch.elapsedTime(TimeUnit.SECONDS), count));
//	}
//
//	/**
//	 * List all files from a directory and its subdirectories
//	 * 
//	 * @param directoryName
//	 *            to be listed
//	 */
//	public void listFilesAndFilesSubDirectories(String directoryName) {
//
//		java.io.File directory = new java.io.File(directoryName);
//
//		// get all the files from a directory
//		java.io.File[] fList = directory.listFiles();
//
//		for (java.io.File file : fList) {
//			if (file.isFile()) {
//				_create(file);
//				// log.info(file.getAbsolutePath());
////				if (count > 50) {
////					return;
////				}
//			} else if (file.isDirectory()) {
//				listFilesAndFilesSubDirectories(file.getAbsolutePath());
//			}
//		}
//	}
//
//	private void _create(java.io.File file) {
//		try {
//			MimeType mimeType = allTypes.getMimeType(file);
//			log.info(mimeType.getName() + " - " + file.getAbsolutePath()
//					+ file.getName());
//			createDocument(file.getName(), mimeType.getName(),
//					file.getAbsolutePath());
//			count++;
//		} catch (Throwable t) {
//			log.error("_create failed", t);
//		}
//
//	}
//
//	@BeforeSuite
//	public void beforeSuite() {
//		log.info("** beforeSuite **");
//	}
//	
//	@AfterSuite
//	public void tearDownSuite() throws Exception {
//		log.info("*** tearDownSuite ***");
//	}
}
