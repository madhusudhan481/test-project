package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.DecorableTSFactory.DecorableTSFBuilder;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;

/**
 * {@link com.fasterxml.jackson.core.TokenStreamFactory.TSFBuilder}
 * implementation for constructing {@link JsonFactory}
 * instances for reading/writing JSON encoded content.
 */
public class JsonFactoryBuilder extends DecorableTSFBuilder<JsonFactory, JsonFactoryBuilder>
{
    protected CharacterEscapes _characterEscapes;

    protected SerializableString _rootValueSeparator;

    protected int _maximumNonEscapedChar;

    public JsonFactoryBuilder() {
        super(JsonFactory.DEFAULT_JSON_PARSER_FEATURE_FLAGS,
                JsonFactory.DEFAULT_JSON_GENERATOR_FEATURE_FLAGS);
        _rootValueSeparator = JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR;
        _maximumNonEscapedChar = 0;
    }

    public JsonFactoryBuilder(JsonFactory base) {
        super(base);
        _characterEscapes = base._characterEscapes;
        _rootValueSeparator = base._rootValueSeparator;
        _maximumNonEscapedChar = base._maximumNonEscapedChar;
    }

    /*
    /**********************************************************
    /* Mutators
    /**********************************************************
     */

    // // // JSON-parsing features

    // // // Parser features

    public JsonFactoryBuilder enable(JsonReadFeature f) {
        _formatReadFeatures |= f.getMask();
        return this;
    }

    public JsonFactoryBuilder enable(JsonReadFeature first, JsonReadFeature... other) {
        _formatReadFeatures |= first.getMask();
        for (JsonReadFeature f : other) {
            _formatReadFeatures |= f.getMask();
        }
        return this;
    }

    public JsonFactoryBuilder disable(JsonReadFeature f) {
        _formatReadFeatures &= ~f.getMask();
        return this;
    }

    public JsonFactoryBuilder disable(JsonReadFeature first, JsonReadFeature... other) {
        _formatReadFeatures &= ~first.getMask();
        for (JsonReadFeature f : other) {
            _formatReadFeatures &= ~f.getMask();
        }
        return this;
    }

    public JsonFactoryBuilder configure(JsonReadFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Generator features

    public JsonFactoryBuilder enable(JsonWriteFeature f) {
        _formatWriteFeatures |= f.getMask();
        return this;
    }

    public JsonFactoryBuilder enable(JsonWriteFeature first, JsonWriteFeature... other) {
        _formatWriteFeatures |= first.getMask();
        for (JsonWriteFeature f : other) {
            _formatWriteFeatures |= f.getMask();
        }
        return this;
    }

    public JsonFactoryBuilder disable(JsonWriteFeature f) {
        _formatWriteFeatures &= ~f.getMask();
        return this;
    }
    
    public JsonFactoryBuilder disable(JsonWriteFeature first, JsonWriteFeature... other) {
        _formatWriteFeatures &= ~first.getMask();
        for (JsonWriteFeature f : other) {
            _formatWriteFeatures &= ~f.getMask();
        }
        return this;
    }

    public JsonFactoryBuilder configure(JsonWriteFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Other JSON-specific configuration

    /**
     * Method for defining custom escapes factory uses for {@link JsonGenerator}s
     * it creates.
     */
    public JsonFactoryBuilder characterEscapes(CharacterEscapes esc) {
        _characterEscapes = esc;
        return this;
    }

    /**
     * Method that allows overriding String used for separating root-level
     * JSON values (default is single space character)
     * 
     * @param sep Separator to use, if any; null means that no separator is
     *   automatically added
     */
    public JsonFactoryBuilder rootValueSeparator(String sep) {
        _rootValueSeparator = (sep == null) ? null : new SerializedString(sep);
        return this;
    }

    /**
     * Method that allows overriding String used for separating root-level
     * JSON values (default is single space character)
     * 
     * @param sep Separator to use, if any; null means that no separator is
     *   automatically added
     */
    public JsonFactoryBuilder rootValueSeparator(SerializableString sep) {
        _rootValueSeparator = sep;
        return this;
    }

    /**
     * Method that allows specifying threshold beyond which all characters are
     * automatically escaped (without checking possible custom escaping settings
     * a la {@link #characterEscapes}: for example, to force escaping of all non-ASCII
     * characters (set to 127), or all non-Latin-1 character (set to 255).
     * Default setting is "disabled", specified by passing value of {@code 0} (or
     * negative numbers).
     *<p>
     * NOTE! Lowest value (aside from marker 0) is 127: for ASCII range, other checks apply
     * and this threshold is ignored.
     * 
     * @param maxNonEscaped Highest character code that is NOT automatically escaped; if
     *    positive value above 0, or 0 to indicate that no automatic escaping is applied
     *    beside from what JSON specification requires (and possible custom escape settings).
     *    Values between 1 and 127 are all taken to behave as if 127 is specified: that is,
     *    no automatic escaping is applied in ASCII range.
     */
    public JsonFactoryBuilder highestNonEscapedChar(int maxNonEscaped) {
        _maximumNonEscapedChar = (maxNonEscaped <= 0) ? 0 : Math.max(127, maxNonEscaped);
        return this;
    }

    // // // Accessors for JSON-specific settings
    
    public CharacterEscapes characterEscapes() { return _characterEscapes; }
    public SerializableString rootValueSeparator() { return _rootValueSeparator; }

    public int highestNonEscapedChar() { return _maximumNonEscapedChar; }

    @Override
    public JsonFactory build() {
        // 28-Dec-2017, tatu: No special settings beyond base class ones, so:
        return new JsonFactory(this);
    }
}
