
package complogin;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the complogin package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidarUsuarioResponse_QNAME = new QName("http://CompLogin/", "validarUsuarioResponse");
    private final static QName _ValidarUsuario_QNAME = new QName("http://CompLogin/", "validarUsuario");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: complogin
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ValidarUsuario }
     * 
     */
    public ValidarUsuario createValidarUsuario() {
        return new ValidarUsuario();
    }

    /**
     * Create an instance of {@link ValidarUsuarioResponse }
     * 
     */
    public ValidarUsuarioResponse createValidarUsuarioResponse() {
        return new ValidarUsuarioResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarUsuarioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://CompLogin/", name = "validarUsuarioResponse")
    public JAXBElement<ValidarUsuarioResponse> createValidarUsuarioResponse(ValidarUsuarioResponse value) {
        return new JAXBElement<ValidarUsuarioResponse>(_ValidarUsuarioResponse_QNAME, ValidarUsuarioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarUsuario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://CompLogin/", name = "validarUsuario")
    public JAXBElement<ValidarUsuario> createValidarUsuario(ValidarUsuario value) {
        return new JAXBElement<ValidarUsuario>(_ValidarUsuario_QNAME, ValidarUsuario.class, null, value);
    }

}
