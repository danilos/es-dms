package test.github.richardwilly98.services;

import static org.elasticsearch.common.io.Streams.copyToBytesFromClasspath;

import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.common.Base64;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.github.richardwilly98.Document;
import com.github.richardwilly98.File;
import com.github.richardwilly98.services.DocumentProvider;

/*
 * https://github.com/shairontoledo/elasticsearch-attachment-tests/blob/master/src/test/java/net/hashcode/esattach/AttachmentTest.java
 */
@Test
public class DocumentProviderTest {

	private static Logger log = Logger.getLogger(DocumentProviderTest.class);

	@BeforeSuite
	public void beforeSuite() throws Exception {
	}

	@BeforeClass
	public void setupServer() {
	}

	@AfterClass
	public void closeServer() {
	}

	private void testCreateDocument(String name, String contentType, String path, String contentSearch) throws Throwable {
		DocumentProvider provider = new DocumentProvider();
		String id = String.valueOf(System.currentTimeMillis());
		byte[] content = copyToBytesFromClasspath(path);
		String encodedContent = Base64.encodeBytes(content);
		int startCount = 0;
		List<Document> documents = provider.getDocuments(contentSearch);
		startCount = documents.size();
		log.info(String.format("startCount: %s", startCount));
		Document document = new Document();
		File file = new File(encodedContent, name, contentType);
		document.setFile(file);
		document.setId(id);
		String newId = provider.createDocument(document);
		log.info(String.format("New document created #%s", newId));
//		documents = provider.getDocuments(contentSearch);
		documents = provider.contentSearch(contentSearch);
		log.info(String.format("Documents count: %s", documents.size()));
		Assert.assertEquals(documents.size() - startCount, 1);
	}

	@Test
	public void testCreateDocument() throws Throwable {
		log.info("Start testCreateDocument");
		testCreateDocument("lorem.pdf", "application/pdf", "/test/github/richardwilly98/services/lorem.pdf", "Lorem ipsum dolor");
		testCreateDocument("test-attachment.html", "text/html", "/test/github/richardwilly98/services/test-attachment.html", "Aliquam");
	}
	
//	@Test
//	public void testHighlightDocument() throws Throwable {
//		log.info("Start testHighlightDocument");
//		String id = String.valueOf(System.currentTimeMillis());
//		String name = "lorem.pdf";
//		String contentType = "application/pdf";
//		byte[] content = copyToBytesFromClasspath("/test/github/richardwilly98/services/lorem.pdf");
//		String encodedContent = Base64.encodeBytes(content);
//		DocumentProvider provider = new DocumentProvider();
//		int startCount = 0;
//		List<Document> documents = provider.getDocuments("Lorem ipsum dolor");
//		startCount = documents.size();
//		Document document = new Document();
//		File file = new File(encodedContent, name, contentType);
//		document.setFile(file);
//		document.setId(id);
//		String newId = provider.createDocument(document);
//		log.info(String.format("New document created #%s", newId));
//		documents = provider.getDocuments("Lorem ipsum dolor");
//		Assert.assertEquals(documents.size() - startCount, 1);
//	}
}