package study.manas;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileRouterDemo {

	private static final long DURATION_MILIS = 2000;
	private static final String SOURCE_FOLDER = "src/main/resources/source-folder";
	private static final String DESTINATION_FOLDER = "src/main/resources/destination-folder";
	
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		final Processor fileProcessor = new Processor() {
			public void process(Exchange exchange) throws Exception {
				String originalFileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
				Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		        String changedFileName = dateFormat.format(date) + originalFileName;
		        exchange.getIn().setHeader(Exchange.FILE_NAME, changedFileName);
			}
		};
		
		
		context.addRoutes( new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://"+ SOURCE_FOLDER+ "?delete=true").process(fileProcessor).to("file://"+ DESTINATION_FOLDER);
			}
		});
		
		context.start();
		Thread.sleep(DURATION_MILIS);
		context.stop();

	}

}
