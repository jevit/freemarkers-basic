package freemarkerEssai;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class MainFreeMarker {

	public static void main(String[] args) {
		/* Create and adjust the configuration */
		Configuration cfg = new Configuration();
		System.out.println(new File("").getAbsolutePath());
		try {
			cfg.setDirectoryForTemplateLoading(new File(""));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			/* Get or create a template */List<Person> userList = new ArrayList<Person>();

			Person tom = new Person("MY", "Tom", 19, 1);
			Person amy = new Person("RH", "Amy", 28, 0);
			Person leo = new Person("GRO", "Leo", 23, 1);
			leo.setRepas(1);
			amy.setH1(1);
			amy.setH2(1);
			leo.setH11(1);
			leo.setH12(1);
			leo.setH13(1);
			leo.setDureePresence("03:00");
			userList.add(tom);
			userList.add(amy);
			userList.add(leo);

			Map<String, Object> variables = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM YYYY");
			variables.put("title", "Pointages journaliers horaires du mercredi " + sdf.format(new Date()));
			variables.put("userList", userList);
			Template tp = cfg.getTemplate("template/planning.ftl");
			StringWriter stringWriter = new StringWriter();
			BufferedWriter writer = new BufferedWriter(stringWriter);
			tp.setEncoding("UTF-8");
			tp.process(variables, writer);
			String htmlStr = stringWriter.toString();
			writer.flush();
			String outputFile = "out/planning.pdf";
			writer.close();
			OutputStream out = new FileOutputStream(outputFile);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(htmlStr.getBytes()));
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(doc, null);

			renderer.layout();
			renderer.createPDF(out);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
