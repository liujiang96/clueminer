package org.clueminer.io.importer.api;

import org.clueminer.dataset.api.AttributeBuilder;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.openide.filesystems.FileObject;

/**
 * ContainerUnloader is responsible for transforming pre-loaded data into real
 * data-structure
 *
 * @author Tomas Barton
 */
public interface ContainerLoader {

    int getInstanceCount();

    Iterable<InstanceDraft> getInstances();

    /**
     * Return instance by index
     *
     * @param index
     * @return
     */
    InstanceDraft getInstance(int index);

    /**
     * Return number of detected attributes in parsed file
     *
     * @return
     */
    int getAttributeCount();

    /**
     * Create attribute draft with given name
     *
     * @param index - position in future dataset
     * @param name  - unique name
     * @return
     */
    AttributeDraft createAttribute(int index, String name);

    /**
     * Return attribute at given index
     *
     * @param index
     * @return
     */
    AttributeDraft getAttribute(int index);

    /**
     * Check whether attribute with given name already exists
     *
     * @param key
     * @return
     */
    boolean hasAttribute(String key);

    /**
     * Basically we check for unique ID column
     *
     * @return true when any of attributes could be a primary key
     */
    boolean hasPrimaryKey();

    /**
     *
     * @return attribute drafts
     */
    Iterable<AttributeDraft> getAttributes();

    /**
     * Adds new Instance draft
     *
     * @param instance
     * @param row      number of row (or other hint like PK)
     */
    void addInstance(InstanceDraft instance, int row);

    /**
     * Text representation of source
     *
     * @return
     */
    String getSource();

    AttributeBuilder getAttributeBuilder();

    void setAttributeBuilder(AttributeBuilder builder);

    void setDataset(Dataset<? extends Instance> dataset);

    Dataset<? extends Instance> getDataset();

    void setFile(FileObject file);

    FileObject getFile();

    /**
     * Number of lines with data
     *
     * @param count
     */
    void setNumberOfLines(int count);

    /**
     * Return number of readable lines in file
     *
     * @return
     */
    int getNumberOfLines();

    /**
     * Default type for all numeric attributes
     *
     * @return
     */
    Object getDefaultNumericType();

    /**
     * Sets default type for all numeric attributes
     *
     * @param type
     */
    void setDefaultNumericType(Class<?> type);

    /**
     * Fetches attribute by a key
     *
     * @param key
     * @param typeClass
     * @return
     */
    AttributeDraft getAttribute(String key, Class typeClass);

    /**
     * Set type of data which could be used for optimization of inner data
     * structure representation
     *
     * @param dataType
     */
    void setDataType(String dataType);

    /**
     * Data type is usually either discrete or continuous
     *
     * @return type of data
     */
    String getDataType();

    /**
     * Should clear already pre-loaded instances
     */
    void reset();

}
