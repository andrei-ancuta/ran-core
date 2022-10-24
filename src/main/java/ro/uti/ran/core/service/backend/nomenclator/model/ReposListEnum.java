package ro.uti.ran.core.service.backend.nomenclator.model;

import ro.uti.ran.core.repository.registru.FructProdRepository;

/**
 * Created by smash on 10/12/15.
 */
public enum ReposListEnum {

    FructProd("FructProd", FructProdRepository.class);

    String className;
    Class repo;

    ReposListEnum(String className, Class repo) {
        this.className = className;
        this.repo = repo;
    }

    public static Class getRepoByCode(String code){
        ReposListEnum[] values = ReposListEnum.values();
        for(ReposListEnum v:values){
            if(v.className.equals(code)){
                return v.repo;
            }
        }
        return null;
    }

    public Class getRepo() {
        return repo;
    }

    public void setRepo(Class repo) {
        this.repo = repo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}


