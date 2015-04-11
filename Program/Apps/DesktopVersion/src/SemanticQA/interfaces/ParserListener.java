package SemanticQA.interfaces;

import java.util.List;

/**
 * interface untuk memonitor proses pembentukan parse tree
 * @author syamsul
 */
public interface ParserListener {
    
    public void onParseSuccess(List parseTree);
    public void onParseFail(String reason);
    
}
