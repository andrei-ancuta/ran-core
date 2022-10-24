package ro.uti.ran.core.audit;

/**
 * Created by Stanciu Neculai on 07.Jan.2016.
 */
public interface DescriereOperatieSesiune {
    String getDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t);

    String getDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Object result);

    String getDescriereCompletaOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t);

    String getDescriereCompletaOpSesiuneFrom(Audit auditMeta, Object[] args, Object result);

    public  String getNomenclatorDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Object result);
}
