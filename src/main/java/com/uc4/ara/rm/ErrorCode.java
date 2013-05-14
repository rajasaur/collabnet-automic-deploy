/**
 * ErrorCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.uc4.ara.rm;

public class ErrorCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ErrorCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _InvalidOptions = "InvalidOptions";
    public static final java.lang.String _InvalidInputFile = "InvalidInputFile";
    public static final java.lang.String _AuthenticationError = "AuthenticationError";
    public static final java.lang.String _AuthorizationError = "AuthorizationError";
    public static final java.lang.String _SchemaValidationError = "SchemaValidationError";
    public static final java.lang.String _BusinessRuleViolation = "BusinessRuleViolation";
    public static final java.lang.String _MaintypeIsRequired = "MaintypeIsRequired";
    public static final java.lang.String _NoMaintypeExpected = "NoMaintypeExpected";
    public static final java.lang.String _InvalidDataFormat = "InvalidDataFormat";
    public static final java.lang.String _MaintypeNotFound = "MaintypeNotFound";
    public static final java.lang.String _CustomTypeNotFound = "CustomTypeNotFound";
    public static final java.lang.String _TypeVersionNotFound = "TypeVersionNotFound";
    public static final java.lang.String _WronglyFormattedDate = "WronglyFormattedDate";
    public static final java.lang.String _InvalidFormatOption = "InvalidFormatOption";
    public static final java.lang.String _InvalidCountOption = "InvalidCountOption";
    public static final java.lang.String _PropertyRuleViolated = "PropertyRuleViolated";
    public static final java.lang.String _DatatypeMismatch = "DatatypeMismatch";
    public static final java.lang.String _ReferenceNotFound = "ReferenceNotFound";
    public static final java.lang.String _PropertyDoesnotExists = "PropertyDoesnotExists";
    public static final java.lang.String _ObjectIdentityAlreadyExists = "ObjectIdentityAlreadyExists";
    public static final java.lang.String _MandatoryPropertiesMissing = "MandatoryPropertiesMissing";
    public static final java.lang.String _InvalidIndentityProperty = "InvalidIndentityProperty";
    public static final java.lang.String _SystemPropertyIsReadOnly = "SystemPropertyIsReadOnly";
    public static final java.lang.String _DuplicateProperty = "DuplicateProperty";
    public static final java.lang.String _WrongFormatPropertyName = "WrongFormatPropertyName";
    public static final java.lang.String _NoPropertyForDeletion = "NoPropertyForDeletion";
    public static final java.lang.String _PropertiesCannotBeUsedTogetherForDeletion = "PropertiesCannotBeUsedTogetherForDeletion";
    public static final java.lang.String _OperatorDoesNotSupport = "OperatorDoesNotSupport";
    public static final java.lang.String _ConditionIsInvalid = "ConditionIsInvalid";
    public static final java.lang.String _ConditionOperatorIsInvalid = "ConditionOperatorIsInvalid";
    public static final java.lang.String _ConditionPropertyDoesnotSupport = "ConditionPropertyDoesnotSupport";
    public static final java.lang.String _FolderLogsIsNotWriteable = "FolderLogsIsNotWriteable";
    public static final java.lang.String _NoCreationRightOnCustomType = "NoCreationRightOnCustomType";
    public static final java.lang.String _NoWriteAccessOnObject = "NoWriteAccessOnObject";
    public static final java.lang.String _NoReadAccessOnObject = "NoReadAccessOnObject";
    public static final java.lang.String _UnexpectedError = "UnexpectedError";
    public static final ErrorCode InvalidOptions = new ErrorCode(_InvalidOptions);
    public static final ErrorCode InvalidInputFile = new ErrorCode(_InvalidInputFile);
    public static final ErrorCode AuthenticationError = new ErrorCode(_AuthenticationError);
    public static final ErrorCode AuthorizationError = new ErrorCode(_AuthorizationError);
    public static final ErrorCode SchemaValidationError = new ErrorCode(_SchemaValidationError);
    public static final ErrorCode BusinessRuleViolation = new ErrorCode(_BusinessRuleViolation);
    public static final ErrorCode MaintypeIsRequired = new ErrorCode(_MaintypeIsRequired);
    public static final ErrorCode NoMaintypeExpected = new ErrorCode(_NoMaintypeExpected);
    public static final ErrorCode InvalidDataFormat = new ErrorCode(_InvalidDataFormat);
    public static final ErrorCode MaintypeNotFound = new ErrorCode(_MaintypeNotFound);
    public static final ErrorCode CustomTypeNotFound = new ErrorCode(_CustomTypeNotFound);
    public static final ErrorCode TypeVersionNotFound = new ErrorCode(_TypeVersionNotFound);
    public static final ErrorCode WronglyFormattedDate = new ErrorCode(_WronglyFormattedDate);
    public static final ErrorCode InvalidFormatOption = new ErrorCode(_InvalidFormatOption);
    public static final ErrorCode InvalidCountOption = new ErrorCode(_InvalidCountOption);
    public static final ErrorCode PropertyRuleViolated = new ErrorCode(_PropertyRuleViolated);
    public static final ErrorCode DatatypeMismatch = new ErrorCode(_DatatypeMismatch);
    public static final ErrorCode ReferenceNotFound = new ErrorCode(_ReferenceNotFound);
    public static final ErrorCode PropertyDoesnotExists = new ErrorCode(_PropertyDoesnotExists);
    public static final ErrorCode ObjectIdentityAlreadyExists = new ErrorCode(_ObjectIdentityAlreadyExists);
    public static final ErrorCode MandatoryPropertiesMissing = new ErrorCode(_MandatoryPropertiesMissing);
    public static final ErrorCode InvalidIndentityProperty = new ErrorCode(_InvalidIndentityProperty);
    public static final ErrorCode SystemPropertyIsReadOnly = new ErrorCode(_SystemPropertyIsReadOnly);
    public static final ErrorCode DuplicateProperty = new ErrorCode(_DuplicateProperty);
    public static final ErrorCode WrongFormatPropertyName = new ErrorCode(_WrongFormatPropertyName);
    public static final ErrorCode NoPropertyForDeletion = new ErrorCode(_NoPropertyForDeletion);
    public static final ErrorCode PropertiesCannotBeUsedTogetherForDeletion = new ErrorCode(_PropertiesCannotBeUsedTogetherForDeletion);
    public static final ErrorCode OperatorDoesNotSupport = new ErrorCode(_OperatorDoesNotSupport);
    public static final ErrorCode ConditionIsInvalid = new ErrorCode(_ConditionIsInvalid);
    public static final ErrorCode ConditionOperatorIsInvalid = new ErrorCode(_ConditionOperatorIsInvalid);
    public static final ErrorCode ConditionPropertyDoesnotSupport = new ErrorCode(_ConditionPropertyDoesnotSupport);
    public static final ErrorCode FolderLogsIsNotWriteable = new ErrorCode(_FolderLogsIsNotWriteable);
    public static final ErrorCode NoCreationRightOnCustomType = new ErrorCode(_NoCreationRightOnCustomType);
    public static final ErrorCode NoWriteAccessOnObject = new ErrorCode(_NoWriteAccessOnObject);
    public static final ErrorCode NoReadAccessOnObject = new ErrorCode(_NoReadAccessOnObject);
    public static final ErrorCode UnexpectedError = new ErrorCode(_UnexpectedError);
    public java.lang.String getValue() { return _value_;}
    public static ErrorCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ErrorCode enumeration = (ErrorCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ErrorCode fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ErrorCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uc4.com/", "ErrorCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
