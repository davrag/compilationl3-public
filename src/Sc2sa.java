
import sc.analysis.*;
import sc.node.*;
import sa.*;

public class Sc2sa extends DepthFirstAdapter {
    private SaNode returnValue;

    private <T> T retrieveNode(Node node) {
        if(node != null)
            node.apply(this);
        return (T)returnValue;
    }

    @Override
    public void caseAAppelfct(AAppelfct node) {
        returnValue = new SaAppel(node.getIdentif().getText(),retrieveNode(node.getListeexp()));
    }

    public void caseAPlusExp3(APlusExp3 node){
        SaExp op1 =null;
        SaExp op2 =null;
        node.getExp3().apply(this);
        op1 = (SaExp)this.returnValue;
        node.getExp4().apply(this);
        op2 = (SaExp)this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);
    }

    public void caseAAppelfctExp6(AAppelfctExp6 node){
        returnValue = new SaExpAppel(retrieveNode(node.getAppelfct()));
    }

    @Override
    public void caseADecvarldecfoncProgramme(ADecvarldecfoncProgramme node) {
        this.returnValue = new SaProg(retrieveNode(node.getOptdecvar()), retrieveNode(node.getListedecfonc()));
    }

    @Override
    public void caseALdecfoncProgramme(ALdecfoncProgramme node) {
        node.getListedecfonc().apply(this);
        SaLDec functions = (SaLDec) this.returnValue;
        this.returnValue = new SaProg(null, functions);
    }

    @Override
    public void caseAOptdecvar(AOptdecvar node) {
        node.getListedecvar().apply(this);
    }

    @Override
    public void caseADecvarldecvarListedecvar(ADecvarldecvarListedecvar node) {
        node.getDecvar().apply(this);
        SaDec variables = (SaDec) this.returnValue;
        node.getListedecvarbis().apply(this);
        SaLDec variablesBis = (SaLDec) this.returnValue;
        this.returnValue = new SaLDec(variables, variablesBis);
    }

    @Override
    public void caseADecvarListedecvar(ADecvarListedecvar node) {
        returnValue = new SaLDec(retrieveNode(node.getDecvar()), null);
    }

    @Override
    public void caseADecvarldecvarListedecvarbis(ADecvarldecvarListedecvarbis node) {
        node.getDecvar().apply(this);
        SaDec variables = (SaDec) this.returnValue;
        node.getListedecvarbis().apply(this);
        SaLDec variablesBis = (SaLDec) this.returnValue;
        this.returnValue = new SaLDec(variables, variablesBis);
    }

    @Override
    public void caseADecvarListedecvarbis(ADecvarListedecvarbis node) {
        returnValue = new SaLDec(retrieveNode(node.getDecvar()), null);
    }

    @Override
    public void caseADecvarentierDecvar(ADecvarentierDecvar node) {
        this.returnValue = new SaDecVar(node.getIdentif().getText());
    }

    @Override
    public void caseADecvartableauDecvar(ADecvartableauDecvar node) {
        this.returnValue = new SaDecTab(node.getIdentif().getText(), Integer.parseInt(node.getNombre().getText()));
    }

    @Override
    public void caseALdecfoncrecListedecfonc(ALdecfoncrecListedecfonc node) {
        node.getDecfonc().apply(this);
        SaDec fonction = (SaDec) this.returnValue;
        node.getListedecfonc().apply(this);
        SaLDec listeFonction = (SaLDec)this.returnValue;
        this.returnValue = new SaLDec(fonction, listeFonction);
    }

    @Override
    public void caseALdecfoncfinalListedecfonc(ALdecfoncfinalListedecfonc node) {
        returnValue = null;
    }

    @Override
    public void caseADecvarinstrDecfonc(ADecvarinstrDecfonc node) {
        SaLDec parametres;
        SaLDec variables;
        SaInst blockInstruction;

        node.getListeparam().apply(this);
        parametres = (SaLDec)returnValue;
        node.getOptdecvar().apply(this);
        variables = (SaLDec)returnValue;
        node.getInstrbloc().apply(this);
        blockInstruction = (SaInst) returnValue;

        returnValue = new SaDecFonc(node.getIdentif().getText(), parametres, variables, blockInstruction);
    }

    @Override
    public void caseAInstrDecfonc(AInstrDecfonc node) {
        returnValue = new SaDecFonc(node.getIdentif().getText(), retrieveNode(node.getListeparam()), null, retrieveNode(node.getInstrbloc()));
    }

    @Override
    public void caseASansparamListeparam(ASansparamListeparam node) {
        returnValue = null;
    }

    @Override
    public void caseAAvecparamListeparam(AAvecparamListeparam node) {
        node.getListedecvar().apply(this);
    }

    @Override
    public void caseAInstraffectInstr(AInstraffectInstr node) {
        node.getInstraffect().apply(this);
        SaInstAffect affect = (SaInstAffect) returnValue;
        returnValue = new SaInstAffect(affect.getLhs(), affect.getRhs());
    }

    @Override
    public void caseAInstrblocInstr(AInstrblocInstr node) {
        node.getInstrbloc().apply(this);
    }

    @Override
    public void caseAInstrsiInstr(AInstrsiInstr node) {
        node.getInstrsi().apply(this);
    }

    @Override
    public void caseAInstrtantqueInstr(AInstrtantqueInstr node) {
        node.getInstrtantque().apply(this);
    }

    @Override
    public void caseAInstrappelInstr(AInstrappelInstr node) {
        node.getInstrappel().apply(this);
    }

    @Override
    public void caseAInstrretourInstr(AInstrretourInstr node) {
        node.getInstrretour().apply(this);
    }

    @Override
    public void caseAInstrecritureInstr(AInstrecritureInstr node) {
        node.getInstrecriture().apply(this);
    }

    @Override
    public void caseAInstrvideInstr(AInstrvideInstr node) {
        node.getInstrvide().apply(this);
    }

    @Override
    public void caseAInstraffect(AInstraffect node) {
        node.getVar().apply(this);
        SaVar variable = (SaVar)returnValue;
        node.getExp().apply(this);
        SaExp expression = (SaExp)returnValue;
        returnValue = new SaInstAffect(variable, expression);
    }

    @Override
    public void caseAInstrbloc(AInstrbloc node) {
        returnValue = new SaInstBloc(retrieveNode(node.getListeinst()));
    }

    @Override
    public void caseALinstrecListeinst(ALinstrecListeinst node) {
        node.getInstr().apply(this);
        SaInst instruction = (SaInst) returnValue;
        node.getListeinst().apply(this);
        SaLInst instructionBlock = (SaLInst) returnValue;
        returnValue = new SaLInst(instruction, instructionBlock);
    }

    @Override
    public void caseALinstfinalListeinst(ALinstfinalListeinst node) {
        returnValue = null;
    }

    @Override
    public void caseAAvecsinonInstrsi(AAvecsinonInstrsi node) {
        node.getExp().apply(this);
        SaExp test = (SaExp)returnValue;
        node.getInstrbloc().apply(this);
        SaInst alors = (SaInst)returnValue;
        node.getInstrsinon().apply(this);
        SaInst sinon = (SaInst)returnValue;
        returnValue = new SaInstSi(test, alors, sinon);
    }

    @Override
    public void caseASanssinonInstrsi(ASanssinonInstrsi node) {
        node.getExp().apply(this);
        SaExp test = (SaExp)returnValue;
        node.getInstrbloc().apply(this);
        SaInst alors = (SaInst)returnValue;
        returnValue = new SaInstSi(test, alors, null);
    }

    @Override
    public void caseAInstrsinon(AInstrsinon node) {
        node.getInstrbloc().apply(this);
    }

    @Override
    public void caseAInstrtantque(AInstrtantque node) {
        node.getExp().apply(this);
        SaExp test = (SaExp)returnValue;
        node.getInstrbloc().apply(this);
        SaInst faire = (SaInst)returnValue;
        returnValue = new SaInstTantQue(test, faire);
    }

    @Override
    public void caseAInstrappel(AInstrappel node) {
        node.getAppelfct().apply(this);
    }

    @Override
    public void caseAInstrretour(AInstrretour node) {
        node.getExp().apply(this);
        SaExp retour = (SaExp) this.returnValue;
        returnValue = new SaInstRetour(retour);
    }

    @Override
    public void caseAInstrecriture(AInstrecriture node) {
        node.getExp().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        this.returnValue = new SaInstEcriture(exp);
    }

    @Override
    public void caseAInstrvide(AInstrvide node) {
        returnValue = null;
    }

    @Override
    public void caseAOuExp(AOuExp node) {
        node.getExp().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp1().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpOr(exp, exp1);
    }

    @Override
    public void caseAExp1Exp(AExp1Exp node) {
        node.getExp1().apply(this);
    }

    @Override
    public void caseAEtExp1(AEtExp1 node) {
        node.getExp1().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp2().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAnd(exp, exp1);
    }

    @Override
    public void caseAExp2Exp1(AExp2Exp1 node) {
        node.getExp2().apply(this);
    }

    @Override
    public void caseAInfExp2(AInfExp2 node) {
        node.getExp2().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp3().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpInf(exp, exp1);
    }

    @Override
    public void caseAEgalExp2(AEgalExp2 node) {
        node.getExp2().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp3().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpEqual(exp, exp1);
    }

    @Override
    public void caseAExp3Exp2(AExp3Exp2 node) {
        node.getExp3().apply(this);
    }

    @Override
    public void caseAMoinsExp3(AMoinsExp3 node) {
        node.getExp3().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp4().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpSub(exp, exp1);
    }

    @Override
    public void caseAExp4Exp3(AExp4Exp3 node) {
        node.getExp4().apply(this);
    }

    @Override
    public void caseAFoisExp4(AFoisExp4 node) {
        node.getExp4().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp5().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpMult(exp, exp1);
    }

    @Override
    public void caseADiviseExp4(ADiviseExp4 node) {
        node.getExp4().apply(this);
        SaExp exp = (SaExp) this.returnValue;
        node.getExp5().apply(this);
        SaExp exp1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpDiv(exp, exp1);
    }

    @Override
    public void caseAExp5Exp4(AExp5Exp4 node) {
        node.getExp5().apply(this);
    }

    @Override
    public void caseANonExp5(ANonExp5 node) {
        node.getExp5().apply(this);
        SaExp expression = (SaExp) this.returnValue;
        this.returnValue = new SaExpNot(expression);
    }

    @Override
    public void caseAExp6Exp5(AExp6Exp5 node) {
        node.getExp6().apply(this);
    }

    @Override
    public void caseANombreExp6(ANombreExp6 node) {
        returnValue = new SaExpInt(Integer.parseInt(node.getNombre().getText()));
    }

    @Override
    public void caseAVarExp6(AVarExp6 node) {
        returnValue = new SaExpVar(retrieveNode(node.getVar()));
    }

    @Override
    public void caseAParenthesesExp6(AParenthesesExp6 node) {
        node.getExp().apply(this);
    }

    @Override
    public void caseALireExp6(ALireExp6 node) {
        node.getLire().apply(this);
    }

    @Override
    public void caseAVartabVar(AVartabVar node) {
        node.getExp().apply(this);
        SaExp indice = (SaExp) this.returnValue;
        this.returnValue = new SaVarIndicee(node.getIdentif().getText(), indice);
    }

    @Override
    public void caseAVarsimpleVar(AVarsimpleVar node) {
        returnValue = new SaVarSimple(node.getIdentif().getText());
    }

    @Override
    public void caseARecursifListeexp(ARecursifListeexp node) {
        node.getExp().apply(this);
        SaExp tete = (SaExp) this.returnValue;
        node.getListeexpbis().apply(this);
        SaLExp queue = (SaLExp) this.returnValue;
        this.returnValue = new SaLExp(tete, queue);
    }

    @Override
    public void caseAFinalListeexp(AFinalListeexp node) {
        returnValue = null;
    }

    @Override
    public void caseAFinalListeexpbis(AFinalListeexpbis node) {
        returnValue = null;
    }

    @Override
    public void caseARecursifListeexpbis(ARecursifListeexpbis node) {
        node.getExp().apply(this);
        SaExp tete = (SaExp) this.returnValue;
        node.getListeexpbis().apply(this);
        SaLExp queue = (SaLExp) this.returnValue;
        this.returnValue = new SaLExp(tete, queue);
    }

    public SaNode getRoot() {
        return returnValue;
    }
}
