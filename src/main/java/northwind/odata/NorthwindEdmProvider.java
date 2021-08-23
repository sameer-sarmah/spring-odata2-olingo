package northwind.odata;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

public class NorthwindEdmProvider extends EdmProvider{
	  
		public static final String ENTITY_CONTAINER="Northwind";
		public static final String NAMESPACE="OData";
		public static final String ENTITY_SET_NAME_CUSTOMERS = "Customers";
		public static final String ENTITY_NAME_CUSTOMER = "Customer";
		public static final FullQualifiedName CUSTOMER_ENTITY_TYPE = new FullQualifiedName(NAMESPACE, ENTITY_NAME_CUSTOMER);
		
		public static final String CUSTOMER_ID="customerID";
		public static final String CUSTOMER_NAME="customerName";
		public static final String PHONE="phone";
		public static final String REGION="region";
		public static final String CITY="city";
		public static final String COUNTRY="country";
		public static final String ZIP="ZIP";

	  @Override
	  public List<Schema> getSchemas() throws ODataException {
	    List<Schema> schemas = new ArrayList<Schema>();

	    Schema schema = new Schema();
	    schema.setNamespace(NAMESPACE);

	    List<EntityType> entityTypes = new ArrayList<EntityType>();
	    entityTypes.add(getEntityType(CUSTOMER_ENTITY_TYPE));
	    schema.setEntityTypes(entityTypes);


	    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
	    EntityContainer entityContainer = new EntityContainer();
	    entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);

	    List<EntitySet> entitySets = new ArrayList<EntitySet>();
	    entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_CUSTOMERS));
	    entityContainer.setEntitySets(entitySets);
	    
	    entityContainers.add(entityContainer);
	    schema.setEntityContainers(entityContainers);

	    schemas.add(schema);

	    return schemas;
	  }

	  @Override
	  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
	    if (NAMESPACE.equals(edmFQName.getNamespace())) {

	      if (CUSTOMER_ENTITY_TYPE.getName().equals(edmFQName.getName())) {

	        // Properties
	        List<Property> properties = new ArrayList<Property>();
	        properties.add(new SimpleProperty().setName(CUSTOMER_ID).setType(EdmSimpleTypeKind.String).setFacets(
	            new Facets().setNullable(false)));
	        properties.add(new SimpleProperty().setName(CUSTOMER_NAME).setType(EdmSimpleTypeKind.String).setFacets(
	            new Facets().setMaxLength(100)));
	        
	        properties.add(new SimpleProperty().setName(PHONE).setType(EdmSimpleTypeKind.String).setFacets(
		            new Facets().setMaxLength(100)));
	        properties.add(new SimpleProperty().setName(REGION).setType(EdmSimpleTypeKind.String).setFacets(
		            new Facets().setMaxLength(100)));
	        properties.add(new SimpleProperty().setName(CITY).setType(EdmSimpleTypeKind.String).setFacets(
		            new Facets().setMaxLength(100)));
	        properties.add(new SimpleProperty().setName(COUNTRY).setType(EdmSimpleTypeKind.String).setFacets(
		            new Facets().setMaxLength(100)));
	        properties.add(new SimpleProperty().setName(ZIP).setType(EdmSimpleTypeKind.String).setFacets(
		            new Facets().setMaxLength(100)));


	        // Key
	        List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
	        keyProperties.add(new PropertyRef().setName(CUSTOMER_ID));
	        Key key = new Key().setKeys(keyProperties);

	        return new EntityType().setName(CUSTOMER_ENTITY_TYPE.getName())
	            .setProperties(properties)
	            .setKey(key);
	      } 
	    }

	    return null;
	  }

	  @Override
	  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
	    if (ENTITY_CONTAINER.equals(entityContainer)) {
	      if (ENTITY_SET_NAME_CUSTOMERS.equals(name)) {
	        return new EntitySet().setName(name).setEntityType(CUSTOMER_ENTITY_TYPE);
	      } 
	    }
	    return null;
	  }

	  @Override
	  public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
	    if (name == null || ENTITY_CONTAINER.equals(name)) {
	      return new EntityContainerInfo().setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);
	    }

	    return null;
	  }
}
