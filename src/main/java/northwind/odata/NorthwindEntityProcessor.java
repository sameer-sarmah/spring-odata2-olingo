package northwind.odata;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import northwind.documents.Customer;
import northwind.exception.CoreException;

@Component
public class NorthwindEntityProcessor extends ODataSingleProcessor {


	private static final String CUSTOMER = "Customer";

	private NorthwindReadHandler readHandler = new NorthwindReadHandler();
	private Map<String,Class> entityNametoClass = new HashMap<>();

	public NorthwindEntityProcessor() {
		super();
		entityNametoClass.put(CUSTOMER, Customer.class);
	}
	
	  @Override
	  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) 
	      throws ODataException {
		    EdmEntitySet entitySet;

		    if (uriInfo.getNavigationSegments().size() == 0) {
		      entitySet = uriInfo.getStartEntitySet();

		      if (NorthwindEdmProvider.ENTITY_SET_NAME_CUSTOMERS.equals(entitySet.getName())) {
		    	  List<Map<String, Object>> data = readHandler.readEntitySet(Customer.class);
		        return EntityProvider.writeFeed(contentType, entitySet, data,
		            EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
		      } 

		      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		    } 
		    throw new ODataNotImplementedException();
	  }
//http://localhost:8080/odata/odata.svc/Products(productID='9')
//http://localhost:8080/odata/odata.svc/Products('9')
	
	 @Override
	  public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException {
		    if (uriInfo.getNavigationSegments().size() == 0) {
		        EdmEntitySet entitySet = uriInfo.getStartEntitySet();

		        if (NorthwindEdmProvider.ENTITY_SET_NAME_CUSTOMERS.equals(entitySet.getName())) {
		          String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
		          Map<String, Object> data = readHandler.readEntity(id, Customer.class);

		          if (data != null) {
		            URI serviceRoot = getContext().getPathInfo().getServiceRoot();
		            ODataEntityProviderPropertiesBuilder propertiesBuilder =
		                EntityProviderWriteProperties.serviceRoot(serviceRoot);

		            return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
		          }
		        }
		        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		      } 
		    throw new ODataNotImplementedException();

	}


	  private String getKeyValue(final KeyPredicate key) throws ODataException {
	    EdmProperty property = key.getProperty();
	    EdmSimpleType type = (EdmSimpleType) property.getType();
	    return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), String.class);
	  }

}
