package com.uc4.ara.collabnet.JArgs;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.uc4.ara.collabnet.Logging.Logger;

public class CmdLineParser {

    /**
     * Base class for exceptions that may be thrown when options are parsed
     */
    public static abstract class OptionException extends Exception {
        OptionException(String msg) { super(msg); }
    }

    /**
     * Thrown when the parsed command-line contains an option that is not
     * recognised. <code>getMessage()</code> returns
     * an error string suitable for reporting the error to the user (in
     * English).
     */
    public static class UnknownOptionException extends OptionException {
        UnknownOptionException( String optionName ) {
            this(optionName, "Unknown option '" + optionName + "'");
        }

        UnknownOptionException( String optionName, String msg ) {
            super(msg);
            this.optionName = optionName;
        }

        /**
         * @return the name of the option that was unknown (e.g. "-u")
         */
        public String getOptionName() {
            return this.optionName;
        }

        private final String optionName;
    }

    /**
     * Thrown when the parsed commandline contains multiple concatenated
     * short options, such as -abcd, where one is unknown.
     * <code>getMessage()</code> returns an english human-readable error
     * string.
     * @author Vidar Holen
     */
    public static class UnknownSuboptionException
        extends UnknownOptionException {
        private char suboption;

        UnknownSuboptionException( String option, char suboption ) {
            super(option, "Illegal option: '"+suboption+"' in '"+option+"'");
            this.suboption=suboption;
        }
        public char getSuboption() {
            return suboption;
        }
    }

    /**
     * Thrown when the parsed commandline contains multiple concatenated
     * short options, such as -abcd, where one or more requires a value.
     * <code>getMessage()</code> returns an english human-readable error
     * string.
     * @author Vidar Holen
     */
    public static class NotFlagException extends UnknownOptionException {
        private char notflag;

        NotFlagException( String option, char unflaggish ) {
            super(option, "Illegal option: '"+option+"', '"+
                  unflaggish+"' requires a value");
            notflag=unflaggish;
        }

        /**
         * @return the first character which wasn't a boolean (e.g 'c')
         */
        public char getOptionChar() {
            return notflag;
        }
    }

    /**
     * Thrown when an illegal or missing value is given by the user for
     * an option that takes a value. <code>getMessage()</code> returns
     * an error string suitable for reporting the error to the user (in
     * English).
     *
     * No generic class can ever extend <code>java.lang.Throwable</code>, so we
     * have to return <code>Option&lt;?&gt;</code> instead of
     * <code>Option&lt;T&gt;</code>.
     */
    public static class IllegalOptionValueException extends OptionException {
        public <T> IllegalOptionValueException( Option<T> opt, String value ) {
            super("Illegal value '" + value + "' for option " +
                  (opt.shortForm() != null ? "-" + opt.shortForm() + ", " : "") +
                  "--" + opt.longForm());
            this.option = opt;
            this.value = value;
        }

        /**
         * @return the name of the option whose value was illegal (e.g. "-u")
         */
        public Option<?> getOption() {
            return this.option;
        }

        /**
         * @return the illegal value
         */
        public String getValue() {
            return this.value;
        }
        private final Option<?> option;
        private final String value;
    }

    /**
     * Representation of a command-line option
     *
     * @param T Type of data configured by this option
     */
    public static abstract class Option<T> {

        protected Option( String longForm, boolean wantsValue, boolean required ) {
            this(null, longForm, wantsValue, required);
        }

        private Option( String shortForm, String longForm, boolean wantsValue, boolean required ) {
            if ( longForm == null ) {
                throw new IllegalArgumentException("Null longForm not allowed");
            }
            this.shortForm = shortForm;
            this.longForm = longForm;
            this.wantsValue = wantsValue;
            this.required = required;
        }

        public boolean isRequired() {
        	return this.required;
        }
        
        public String shortForm() {
            return this.shortForm;
        }

        public String longForm() {
            return this.longForm;
        }

        /**
         * Tells whether or not this option wants a value
         */
        public boolean wantsValue() {
            return this.wantsValue;
        }

        public final T getValue( String arg, Locale locale )
            throws IllegalOptionValueException {
            if ( this.wantsValue ) {
                if ( arg == null ) {
                    throw new IllegalOptionValueException(this, "");
                }
                return this.parseValue(arg, locale);
            } else {
                return this.getDefaultValue();
            }
        }

        /**
         * Override to extract and convert an option value passed on the
         * command-line
         */
        protected T parseValue(String arg, Locale locale)
                throws IllegalOptionValueException {

            return null;
        }

        /**
         * Override to define default value returned by getValue if option does
         * not want a value
         */
        protected T getDefaultValue() {
            return null;
        }

        private final String shortForm;
        private final String longForm;
        private final boolean wantsValue;
        private final boolean required;



        /**
         * An option that expects a boolean value
         */
        public static class BooleanOption extends Option<Boolean> {
            public BooleanOption( String shortForm, String longForm, boolean required ) {
                super(shortForm, longForm, false, required);
            }
            public BooleanOption( String longForm , boolean required) {
                super(longForm, false, required);
            }

            @Override
            public Boolean parseValue(String arg, Locale lcoale) {
                return Boolean.TRUE;
            }

            @Override
            public Boolean getDefaultValue() {
                return Boolean.TRUE;
            }
        }

        /**
         * An option that expects an integer value
         */
        public static class IntegerOption extends Option<Integer> {
            public IntegerOption( String shortForm, String longForm, boolean required ) {
                super(shortForm, longForm, true, required);
            }
            public IntegerOption( String longForm, boolean required ) {
                super(longForm, true, required);
            }

            @Override
            protected Integer parseValue( String arg, Locale locale )
                throws IllegalOptionValueException {
                try {
                    return new Integer(arg);
                } catch (NumberFormatException e) {
                    throw new IllegalOptionValueException(this, arg);
                }
            }
        }

        /**
         * An option that expects a long integer value
         */
        public static class LongOption extends Option<Long> {
            public LongOption( String shortForm, String longForm, boolean required ) {
                super(shortForm, longForm, true, required);
            }
            public LongOption( String longForm, boolean required ) {
                super(longForm, true, required);
            }

            @Override
            protected Long parseValue( String arg, Locale locale )
                throws IllegalOptionValueException {
                try {
                    return new Long(arg);
                } catch (NumberFormatException e) {
                    throw new IllegalOptionValueException(this, arg);
                }
            }
        }

        /**
         * An option that expects a floating-point value
         */
        public static class DoubleOption extends Option<Double> {
            public DoubleOption( String shortForm, String longForm, boolean required ) {
                super(shortForm, longForm, true, required);
            }
            public DoubleOption( String longForm, boolean required ) {
                super(longForm, true, required);
            }

            @Override
            protected Double parseValue( String arg, Locale locale )
                throws IllegalOptionValueException {
                try {
                    NumberFormat format = NumberFormat.getNumberInstance(locale);
                    Number num = (Number)format.parse(arg);
                    return new Double(num.doubleValue());
                } catch (ParseException e) {
                    throw new IllegalOptionValueException(this, arg);
                }
            }
        }

        /**
         * An option that expects a string value
         */
        public static class StringOption extends Option<String> {
            public StringOption( String shortForm, String longForm, boolean required ) {
                super(shortForm, longForm, true, required);
            }
            public StringOption( String longForm, boolean required ) {
                super(longForm, true, required);
            }

            @Override
            protected String parseValue( String arg, Locale locale ) {
                return arg;
            }
        }
    }

    /**
     * Add the specified Option to the list of accepted options
     */
    public final <T> Option<T> addOption( Option<T> opt ) {
        if ( opt.shortForm() != null ) {
            this.options.put("-" + opt.shortForm(), opt);
        }
        this.options.put("--" + opt.longForm(), opt);
        return opt;
    }

    /**
     * Convenience method for adding a string option.
     * @return the new Option
     */
    public final Option<String> addStringOption( String shortForm, String longForm, boolean required) {
        return addOption(new Option.StringOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a string option.
     * @return the new Option
     */
    public final Option<String> addStringOption( String longForm, boolean required ) {
        return addOption(new Option.StringOption(longForm, required));
    }

    /**
     * Convenience method for adding an integer option.
     * @return the new Option
     */
    public final Option<Integer> addIntegerOption( String shortForm, String longForm, boolean required ) {
        return addOption(new Option.IntegerOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding an integer option.
     * @return the new Option
     */
    public final Option<Integer> addIntegerOption( String longForm, boolean required ) {
        return addOption(new Option.IntegerOption(longForm, required));
    }

    /**
     * Convenience method for adding a long integer option.
     * @return the new Option
     */
    public final Option<Long> addLongOption( String shortForm, String longForm, boolean required ) {
        return addOption(new Option.LongOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a long integer option.
     * @return the new Option
     */
    public final Option<Long> addLongOption( String longForm, boolean required ) {
        return addOption(new Option.LongOption(longForm, required));
    }

    /**
     * Convenience method for adding a double option.
     * @return the new Option
     */
    public final Option<Double> addDoubleOption( String shortForm, String longForm, boolean required ) {
        return addOption(new Option.DoubleOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a double option.
     * @return the new Option
     */
    public final Option<Double> addDoubleOption( String longForm, boolean required ) {
        return addOption(new Option.DoubleOption(longForm, required));
    }

    /**
     * Convenience method for adding a boolean option.
     * @return the new Option
     */
    public final Option<Boolean> addBooleanOption( String shortForm, String longForm, boolean required ) {
        return addOption(new Option.BooleanOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a boolean option.
     * @return the new Option
     */
    public final Option<Boolean> addBooleanOption( String longForm, boolean required ) {
        return addOption(new Option.BooleanOption(longForm, required));
    }

    /**
     * Equivalent to {@link #getOptionValue(Option, Object) getOptionValue(o,
     * null)}.
     * @throws Exception 
     */
    public final <T> T getOptionValue( Option<T> o ) throws Exception {
        return getOptionValue(o, null);
    }


    /**
     * @return the parsed value of the given Option, or the given default 'def'
     * if the option was not set
     * @throws Exception 
     */
    public final <T> T getOptionValue( Option<T> o, T def ) throws Exception {
         List<?> v = values.get(o.longForm());

        if (v == null) {
        	if(o.isRequired())
        		throw new IllegalStateException("Argument -" + o.shortForm() + ", --" + o.longForm() + " is required");
            return def;
        } else if (v.isEmpty()) {
        	if(o.isRequired())
        		throw new IllegalStateException("Argument -" + o.shortForm() + ", --" + o.longForm() + " is required");
            return null;
        } else {

            /* Cast should be safe because Option.parseValue has to return an
             * instance of type T or null
             */
            @SuppressWarnings("unchecked")
            T result = (T)v.remove(0);
            return result;
        }
    }


    /**
     * @return A Collection giving the parsed values of all the occurrences of
     * the given Option, or an empty Collection if the option was not set.
     * @throws Exception 
     */
    public final <T> Collection<T> getOptionValues(Option<T> option) throws Exception {
        Collection<T> result = new ArrayList<T>();

        while (true) {
            T o = getOptionValue(option, null);

            if (o == null) {
                return result;
            } else {
                result.add(o);
            }
        }
    }


    /**
     * @return the non-option arguments
     */
    public final String[] getRemainingArgs() {
        return this.remainingArgs;
    }

    /**
     * Extract the options and non-option arguments from the given
     * list of command-line arguments. The default locale is used for
     * parsing options whose values might be locale-specific.
     */
    public final void parse( String[] argv ) throws OptionException {
        parse(argv, Locale.getDefault());
    }

    /**
     * Extract the options and non-option arguments from the given
     * list of command-line arguments. The specified locale is used for
     * parsing options whose values might be locale-specific.
     */
    public final void parse( String[] argv, Locale locale )
            throws OptionException {

        ArrayList<Object> otherArgs = new ArrayList<Object>();
        int position = 0;
        this.values = new HashMap<String, List<?>>(10);
        while ( position < argv.length ) {
            String curArg = argv[position];
            if ( curArg.startsWith("-") ) {
                if ( curArg.equals("--") ) { // end of options
                    position += 1;
                    break;
                }
                String valueArg = null;
                
                int equalsPos = curArg.indexOf("=");
                if ( equalsPos != -1 ) {
                    valueArg = curArg.substring(equalsPos+1);
                    curArg = curArg.substring(0,equalsPos);
                }
           

                Option<?> opt = this.options.get(curArg);
                if ( opt == null ) {
                    throw new UnknownOptionException(curArg);
                }

                if ( opt.wantsValue() ) {
                    if ( valueArg == null ) {
                        position += 1;
                        if ( position < argv.length ) {
                            valueArg = argv[position];
                        }
                    }
                    addValue(opt, valueArg, locale);
                } else {
                    addValue(opt, null, locale);
                }

                position += 1;
            }
            else {
                otherArgs.add(curArg);
                position += 1;
            }
        }
        for ( ; position < argv.length; ++position ) {
            otherArgs.add(argv[position]);
        }

        this.remainingArgs = new String[otherArgs.size()];
        remainingArgs = otherArgs.toArray(remainingArgs);
    }


    private <T> void addValue(Option<T> opt, String valueArg, Locale locale)
            throws IllegalOptionValueException {

        T value = opt.getValue(valueArg, locale);
        String lf = opt.longForm();

        /* Cast is typesafe because the only location we add elements to the
         * values map is in this method.
         */
        @SuppressWarnings("unchecked")
        List<T> v = (List<T>) values.get(lf);

        if (v == null) {
            v = new ArrayList<T>();
            values.put(lf, v);
        }

        v.add(value);
    }

    
    public <T> Option<T> addHelp(Option<T> option, String helpString) {
    	if(option.isRequired())
    		optionHelpStringsRequired.add("\t-" + option.shortForm() + ", --" + option.longForm() + "\r\n\t\t" + helpString + "\r\n");
    	else 
    		optionHelpStringsOptional.add("\t-" + option.shortForm() + ", --" + option.longForm() + "\r\n\t\t" + helpString + "\r\n");
        return option;
    }
    
    public void setExamples(String examples)
    {
    	this.examples = examples;
    }

    public void printUsage(String feature) {
        Logger.logMsg("\r\nUSAGE: " + feature + " [arguments]");
        
        if(optionHelpStringsRequired.size() > 0)
        	Logger.logMsg("\r\nREQUIRED ARGUMENTS:\r\n");
        
        for (String help : optionHelpStringsRequired) {
        	Logger.logMsg(help);
        }
        
        if(optionHelpStringsOptional.size() > 0)
        	Logger.logMsg("\r\nOPTIONAL ARGUMENTS:\r\n");
        
        for (String help : optionHelpStringsOptional) {
        	Logger.logMsg(help);
        }
        
        if(examples != null && examples.length() > 0) {
        	Logger.logMsg("\r\nEXAMPLES:\r\n");
        	Logger.logMsg(examples);
        }
    }

    private String[] remainingArgs = null;
    private Map<String, Option<?>> options = new HashMap<String, Option<?>>(10);
    private Map<String, List<?>> values = new HashMap<String, List<?>>(10);
    List<String> optionHelpStringsRequired = new ArrayList<String>();
    List<String> optionHelpStringsOptional = new ArrayList<String>();
    String examples;
}


