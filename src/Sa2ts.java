import sa.*;
import ts.*;
import sc.analysis.*;
import sc.node.*;



public class Sa2ts extends SaDepthFirstVisitor <Void>{
    Ts Table_De_Symbole_Globale;
    Ts Table_locale = null;

    public Sa2ts(SaNode SaRoot)
    {
        System.out.print("NOUVELLE TS");
        Table_De_Symbole_Globale = new Ts();
        SaRoot.accept(this);
        Table_locale = null;
    }

    public boolean existeVAR(String identif)
    {
        boolean existed=false;
        if (Table_locale.variables.containsKey(identif))
            existed=true;
        return existed;
    }

    public boolean existeVARglob(String identif)
    {
        boolean existed=false;
        if (Table_De_Symbole_Globale.variables.containsKey(identif))
            existed=true;
        return existed;
    }


    public boolean existeFONC(String identif)
    {
        boolean existed=false;
        if (Table_De_Symbole_Globale.fonctions.containsKey(identif))
            existed=true;
        return existed;
    }

    @Override
    public Void visit(SaDecVar node) {
        System.out.println("DECVAR");
        if (existeVAR(node.tsItem.identif)){
            System.out.println("FAILED");
            System.exit(1);
        }
        if (Table_locale==null)
        {
             node.tsItem = Table_De_Symbole_Globale.addParam(node.getNom());
		}
		else
		{
		    node.tsItem = Table_locale.addParam(node.getNom());
		}
        return null;
    }

    @Override
    public Void visit(SaDecTab node) {
        System.out.println("DECTAB");
        if (Table_locale != null){
            System.out.println("FAILED");
            System.exit(1);
        }

        if (existeVARglob(node.getNom())){
            System.out.println("FAILED");
            System.exit(1);
        }

        node.tsItem = Table_De_Symbole_Globale.addVar(node.getNom(),node.getTaille());

        return null;
    }

    @Override
    public Void visit(SaDecFonc node) {
        System.out.println("DECFONC");
        if (Table_locale!=null) {
            System.out.println("FAILED");
            System.exit(1);
        }
        System.out.println("TEST 1 DECFONC REUSSI");

        System.out.println("oui");
        System.out.println("oui");
        System.out.println("oui");

        if (existeFONC(node.getNom())){
            System.out.println("FAILED");
            System.exit(1);
        }
        System.out.println("TEST 2 DECFONC REUSSI");

        this.Table_locale = new Ts();
        System.out.println("table locale crée");
        int temp=0;
        //Problème dans les get parameter/variable ou corps
        if (node.getParametres()!=null) {
            node.getParametres().accept(this);
            temp = node.getParametres().length();
        }
        if (node.getVariable()!=null)
        {
            node.getVariable().accept(this);
        }
        if (node.getCorps()!=null)
        {
            node.getCorps().accept(this);
        }
        node.tsItem = Table_De_Symbole_Globale.addFct(node.getNom(),temp,this.Table_locale,node);
        System.out.println("tsItem crée");

        System.out.println("Mise a null de la table locale");
        this.Table_locale=null;
        System.out.println("Table local = null" );

        return null;
    }

    @Override
    public Void visit(SaVarSimple node) {
        if (!existeVAR(node.tsItem.identif) && !existeVARglob(node.tsItem.identif))
            System.exit(1);
        /*
        if (existeVAR(node.tsItem.identif)) // ??
        {
            if (Table_locale.getVar(node.tsItem.identif).getTaille()!=1)
            {
                System.exit(1);
            }
        }
        if (existeVARglob(node.tsItem.identif)) // ??
        {
            if (Table_De_Symbole_Globale.getVar(node.tsItem.identif).getTaille()!=1)
                System.exit(1);
        }
        */
        return null;
	}
    
    @Override 
    public Void visit(SaVarIndicee node) {
        if (!existeVAR(node.tsItem.identif) && !existeVARglob(node.tsItem.identif)){
            System.out.println("FAILED");
            System.exit(1);
        }
        /*
        if (existeVAR(node.tsItem.identif)) // ??
        {
            if (Table_locale.getVar(node.tsItem.identif).getTaille()==1)
                {
				    System.exit(1);
				}
        }
        if (existeVARglob(node.tsItem.identif)) // ??
        {
            if (Table_De_Symbole_Globale.getVar(node.tsItem.identif).getTaille()==1)
                System.exit(1);
        }
        */
		return null;
	}

	@Override
    public Void visit(SaAppel node ){
        if (!existeFONC(node.tsItem.identif)){
            System.out.println("FAILED");
            System.exit(1);
        }
        if (!existeFONC("main")){
            System.out.println("FAILED");
            System.exit(1);
        }
        if (Table_De_Symbole_Globale.getFct("main").getTaille()!=0){
            System.out.println("FAILED");
            System.exit(1);
        }
        if (Table_De_Symbole_Globale.getFct(node.tsItem.identif).getTaille()!=node.tsItem.getTaille()){
            System.out.println("FAILED");
            System.exit(1);
        }
        return null;
    }

    public Ts getTableGlobale()
    {
        System.out.println("SUCCESS");
        return Table_De_Symbole_Globale;
    }
}
