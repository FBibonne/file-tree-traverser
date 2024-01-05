package bibonne.files.sizebrowsing;

import java.util.function.Predicate;


public class Bug {

    public void main(){
        var b=WithIllegalFieldName.TRUE;
        System.out.println(b.test.test("false"));
    }


    private enum WithIllegalFieldName{
        TRUE(_ -> true),
        FALSE(_ -> false),
        LOWER(s -> s!=null && s.toLowerCase().equals(s));

        private Predicate<String> test;

        WithIllegalFieldName(Predicate<String> t){
            test=t;
        }
    }
}
