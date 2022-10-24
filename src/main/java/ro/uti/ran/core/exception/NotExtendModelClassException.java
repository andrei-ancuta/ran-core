package ro.uti.ran.core.exception;

/**
 * Created by adrian.boldisor on 3/15/2016.
 */
public class NotExtendModelClassException extends RuntimeException {

    public NotExtendModelClassException(String message)
    {super(message);}

    public NotExtendModelClassException(Object model)
    {super("Clasa :" + model.getClass().getName()+ " trebuie sa fie extinsa de la ro.uti.ran.core.model.Model class");}


    public NotExtendModelClassException()
    {super();}
}
