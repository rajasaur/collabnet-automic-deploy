/**
 * Error.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.uc4.ara.rm;

public class Error  implements java.io.Serializable {
    private com.uc4.ara.rm.ErrorCode errorCode;

    private java.lang.String errorTitle;

    private java.lang.String errorDetail;

    public Error() {
    }

    public Error(
           com.uc4.ara.rm.ErrorCode errorCode,
           java.lang.String errorTitle,
           java.lang.String errorDetail) {
           this.errorCode = errorCode;
           this.errorTitle = errorTitle;
           this.errorDetail = errorDetail;
    }


    /**
     * Gets the errorCode value for this Error.
     * 
     * @return errorCode
     */
    public com.uc4.ara.rm.ErrorCode getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this Error.
     * 
     * @param errorCode
     */
    public void setErrorCode(com.uc4.ara.rm.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the errorTitle value for this Error.
     * 
     * @return errorTitle
     */
    public java.lang.String getErrorTitle() {
        return errorTitle;
    }


    /**
     * Sets the errorTitle value for this Error.
     * 
     * @param errorTitle
     */
    public void setErrorTitle(java.lang.String errorTitle) {
        this.errorTitle = errorTitle;
    }


    /**
     * Gets the errorDetail value for this Error.
     * 
     * @return errorDetail
     */
    public java.lang.String getErrorDetail() {
        return errorDetail;
    }


    /**
     * Sets the errorDetail value for this Error.
     * 
     * @param errorDetail
     */
    public void setErrorDetail(java.lang.String errorDetail) {
        this.errorDetail = errorDetail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Error)) return false;
        Error other = (Error) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errorCode==null && other.getErrorCode()==null) || 
             (this.errorCode!=null &&
              this.errorCode.equals(other.getErrorCode()))) &&
            ((this.errorTitle==null && other.getErrorTitle()==null) || 
             (this.errorTitle!=null &&
              this.errorTitle.equals(other.getErrorTitle()))) &&
            ((this.errorDetail==null && other.getErrorDetail()==null) || 
             (this.errorDetail!=null &&
              this.errorDetail.equals(other.getErrorDetail())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getErrorCode() != null) {
            _hashCode += getErrorCode().hashCode();
        }
        if (getErrorTitle() != null) {
            _hashCode += getErrorTitle().hashCode();
        }
        if (getErrorDetail() != null) {
            _hashCode += getErrorDetail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Error.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uc4.com/", "Error"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uc4.com/", "ErrorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uc4.com/", "ErrorCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uc4.com/", "ErrorTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uc4.com/", "ErrorDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
