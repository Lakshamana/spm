package br.ufpa.labes.spm.util.i18n;

import java.util.Map;

public class Messages {

  private static Map<String, String> mensagens;

  static {
    //		mensagens = new HashMap<String, String>();
    mensagens.put(
        "facades.DynamicModeling.ModelingExcNotDelConAlrStart",
        "Não é possível remover conexões ligadas a atividades já iniciadas!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccProcess",
        "Acesso à base de dados para o processo");
    mensagens.put("facades.DynamicModeling.DaoExcFailed", " falhou. Motivo: ");
    mensagens.put("facades.DynamicModeling.DaoExcProcess", "Processo");
    mensagens.put("facades.DynamicModeling.DaoExc_NotFound", " não encontrado!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccActiv",
        "Acesso à base de dados para a atividade");
    mensagens.put("facades.DynamicModeling.DaoExcDecomActv", "Atividade Decomposta");
    mensagens.put("facades.DynamicModeling.DaoExcNotFound", " não encontrado!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcNotAdded",
        "Atividade não adicionada! Modelo de Processo não Instanciado ou Executando!");
    mensagens.put("facades.DynamicModeling.ModelingExcActv", "Atividade ");
    mensagens.put("facades.DynamicModeling.ModelingExcIsAlrInModel", "' já está no modelo!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcIsNOtReadyToIn",
        " não está pronto para inserir esta atividade!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccActv",
        "Acesso à base de dados para a atividade ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcIsAlrDecActv", " já é uma Atividade Decomposta!");
    mensagens.put("facades.DynamicModeling.ModelingExcIsAlrRun", " já está rodando!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcIsAlrAutoActv", " já é uma Atividade Automática!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcHasNoRefProcMod",
        " sem referência ao Modelo de Processo!");
    mensagens.put("facades.DynamicModeling.ModelingExcHasRefProcModDef", " está em execução!");
    mensagens.put("facades.DynamicModeling.ModelingExcIsAlrNorActv", "já é uma Atividade Normal!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccArtf", "Acesso à base de dados para o artefato ");
    mensagens.put("facades.DynamicModeling.DaoExcArtf", "Artefato");
    mensagens.put("facades.DynamicModeling.ModelingExcAlreadyCont", " já contém");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThisArtfInput", "este Artefato é uma Entrada!");
    mensagens.put("facades.DynamicModeling.NotifyAgtNewInpArtf", "Novo Artefato de Entrada.");
    mensagens.put("facades.DynamicModeling.ModelingExcThisArtfOut", "este Artefato é uma saída!");
    mensagens.put("facades.DynamicModeling.NotifyAgtRemInpArtf", "Artefato de Entrada Removido. ");
    mensagens.put("facades.DynamicModeling.NotifyAgtRemOutArtf", "Artefato de Saída Removido. ");
    mensagens.put("facades.DynamicModeling.NotifyAgtNewOutArtf", "Novo Artefato de Saída.");
    mensagens.put("facades.DynamicModeling.ModelingExcTheActv", " A Atividade ");
    mensagens.put("facades.DynamicModeling.ModelingExcStateNotOk", " estado não APROVADO");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcForNewOutArtf", " para um novo artefato de saída.");
    mensagens.put("facades.DynamicModeling.ModelingExcHasAlrStart", "' já foi iniciada!");
    mensagens.put("facades.DynamicModeling.NotifyAgtActvDelet", "Atividade Deletada!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccArtfConn",
        "Acesso à base de dados para a conexão de artefato ");
    mensagens.put("facades.DynamicModeling.DaoExcArtfConn", "Conexão do Artefato ");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccArtfType",
        "Acesso à base de dados para o tipo do artefato ");
    mensagens.put("facades.DynamicModeling.DaoExcArtfTyp", "Tipo do Artefato  ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheArtfTypeFromArtf",
        "O Tipo de Artefato deste Artefato e da Conexão de Artefato não combinam!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheActvStateDoes",
        "O estado da Atividade não Combina!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheActvAlrHasArtf",
        "A atividade já possui a conexão do artefato");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheActvShouldNormDecomp",
        "A atividade deve ser normal ou Decomposta!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheArtfTypeOfArtfConn",
        "O Tipo de Artefato da Conexão do Artefato deve ser definido antes de ser criado!");
    mensagens.put("facades.DynamicModeling.ModelingExcTheArtfConn", "A Conexão do Artefato ");
    mensagens.put("facades.DynamicModeling.ModelingExcCannBeResolv", " não pode ser removido. n");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcItsNotConnActv", "Não está conectado à atividade ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcItsSourceEnact", "Este codigo está executando!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheActvAlreadyConn",
        "A atividade já está conectada à conexão do artefato!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheDecompActvState",
        "O estado da atividade decomposta não combina!");
    mensagens.put("facades.DynamicModeling.DaoExcMultConn", "Conexão Multipla ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcItsSourNotContains",
        "Its source does not contains it!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccConn", "Acesso à base de dados para a Conexão ");
    mensagens.put("facades.DynamicModeling.DaoExcConnection", "Conexão");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheProcessModelState",
        "O estado do Modelo de Processo não é APROVADO!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheSourceDestinyConn",
        "A fonte e o destino da conexão são a mesma!");
    mensagens.put("facades.DynamicModeling.NotifyAgtActvNotReady", "Atividade nao pronta!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheCombinActvState",
        "A combinação dos estados das atividades ou controle de fluxo não APROVADO.");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThereControlFlowBetw",
        "Não existe nenhum fluxo de controle entre ambas as atividades.");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheCombinActvStates",
        "A combinação dos estados das atividades não é APROVADO.");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccMultConn",
        "Acesso à base de dados para a Conexão Multipla ");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccActivi",
        "Acesso à base de dados para a Atividade");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccJoin", "Acesso à base de dados para o Join ");
    mensagens.put("facades.DynamicModeling.DaoExcJoin", "Join");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThereControlFlow",
        "Já existe um fluxo de controle estabelecido!");
    mensagens.put("facades.DynamicModeling.ModelingExcJoinIsNotOK", "Join não APROVADO!");
    mensagens.put("facades.DynamicModeling.DaoExcBranch", "Branch");
    mensagens.put("facades.DynamicModeling.ModelingExcBranchIsNotOK", "Branch não APROVADO!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThisActvAlreadyConn",
        "Esta Atividade já está conectada a este Branch AND!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheDependOrContr",
        "As Dependências ou o fluxo de controle não são APROVADOS! ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThisMultConnIsAlreConn",
        "Esta Conexão Multipla já está concectada a este Branch AND!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcThereIsContrFlowAlr",
        "Já existe um fluxo de controle estabelecido!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccRole", "Acesso à base de dados para o Cargo ");
    mensagens.put("facades.DynamicModeling.DaoExcRole", "Cargo ");
    mensagens.put("facades.DynamicModeling.ModelingExcHasAlreadyFinis", " já terminou!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccGroupType",
        "Acesso à base de dados para o Tipo do Grupo");
    mensagens.put("facades.DynamicModeling.DaoExcGroupType", "Tipo do Grupo");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccAgent", "Acesso à base de dados para Agente");
    mensagens.put("facades.DynamicModeling.DaoExcAgent", "Agente ");
    mensagens.put("facades.DynamicModeling.ModelingExcTheagent", "O agente");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcAlreadyAllocToActv", " já está alocado à atividade");
    mensagens.put("facades.DynamicModeling.NotifyAgentActvAdded", "Atividade Adicionada ");
    mensagens.put("facades.DynamicModeling.NotifyAgentActivityRemoved", "Atividade Removida!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccGroup", "Acesso à base de dados para o Grupo");
    mensagens.put("facades.DynamicModeling.DaoExcGroup", "Grupo");
    mensagens.put("facades.DynamicModeling.ModelingExcTheGroup", "O grupo");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcIsAlreAllocToActv", " já está alocado à atividade");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccResourceType",
        "Acesso à base de dados para o Tipo do Recurso ");
    mensagens.put("facades.DynamicModeling.DaoExcResourType", "Tipo do Recurso");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheOldRequiResoType",
        "O Antigo Tipo do Recurso Requerido não está alocado para a atividade!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheNewRequiResouType",
        "O Novo Tipo do Recurso Requerido está alocado para a atividade!");
    mensagens.put("facades.DynamicModeling.DynamicModeling.DaoExcResource", "Recurso ");
    mensagens.put("facades.DynamicModeling.ModelingExcTheResource", "O recurso ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcTheRequiTypeDoesNot",
        "O tipo requerido não combina com o tipo do recurso!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcHasNoResoTypeDef",
        " não ha nenhum tipo de recurso definido!");
    mensagens.put("facades.DynamicModeling.ModelingExcAlreHasTheResou", " já possui o recurso!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccResource",
        "Acesso de banco de dados para Recurso");
    mensagens.put("facades.DynamicModeling.NotifyAgentRemoved", " Removida!");
    mensagens.put("facades.DynamicModeling.ModelingExcDoesntHasResource", " não possui o recurso!");
    mensagens.put(
        "facades.DynamicModeling.DaoExcDatabaseAccConsuResource",
        "Acesso à base de dados para o Recurso Consumível ");
    mensagens.put("facades.DynamicModeling.DaoExcConsumableResource", "Recurso Consumível ");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcItIsNotAllowedChange",
        "Não é permitida a mudança da quantidade necessária após o término da atividade!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcActsFromBANDToJXOR",
        "Atividades vindas de conexões Branch AND não podem ir para conexões Join XOR!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcActsFromBANDToJXORStart",
        " Activities from Branch AND Connections can not let to a Join XOR Connection with start-start dependency!");
    mensagens.put(
        "facades.DynamicModeling.ModelingExcLastAgent",
        "Não é possível remover este agente, pois este é o último da atividade ne a atividade está pronta ou ativa. Tente adicionar outro e remover este.");
    mensagens.put(
        "facades.DynamicModeling.ReplanningFinishedActivity",
        "Não é possível replanejar atividades finalizadas!");

    mensagens.put(
        "facades.EnactmentEngine.WAExcRedoFinishedProcMod",
        "Não é possível refazer atividades em modelos de processo finalizados.");
    mensagens.put(
        "facades.EnactmentEngine.WAExcRedoNotEnactActivity",
        "Não é possível refazer atividades de processos que não estão em execução.");
    mensagens.put(
        "facades.EnactmentEngine.DBExcDatabaseAccProc", "Acesso à base de dados para o processo");
    mensagens.put("facades.EnactmentEngine.DBExcFailedRea", " falhou. Motivo:");
    mensagens.put("facades.EnactmentEngine.DBExcProcess", "Processo");
    mensagens.put("facades.EnactmentEngine.DBExcDoesNotExist", " não existe.");
    mensagens.put("facades.EnactmentEngine.UserExcProcess", "Processo ");
    mensagens.put("facades.EnactmentEngine.UserExcIsAlreEnac", " já está executando.");
    mensagens.put(
        "facades.EnactmentEngine.DBExcDatabaseAccActv", "Acesso à base de dados para a atividade");
    mensagens.put(
        "facades.EnactmentEngine.DBExcDatebaseAccAgt", "Acesso à base de dados para o agente ");
    mensagens.put("facades.EnactmentEngine.DBExcActivity", "Atividade ");
    mensagens.put("facades.EnactmentEngine.DBExcAgent", "Agente ");
    mensagens.put("facades.EnactmentEngine.UserExcActv", "Atividade ");
    mensagens.put("facades.EnactmentEngine.UserExcIsntANormal", " não é uma Atividade Normal!");
    mensagens.put("facades.EnactmentEngine.UserExcAgt", "Agente ");
    mensagens.put("facades.EnactmentEngine.UserExcIsNotReqFor", " não requerido para a atividade ");
    mensagens.put(
        "facades.EnactmentEngine.UserExcIsNotReadyFor", " não está pronto para a tarefa.");
    mensagens.put(
        "facades.EnactmentEngine.UserExcOneOrMoreReq",
        "Um ou mais recursos requeridos são indisponíveis.");
    mensagens.put(
        "facades.EnactmentEngine.UserExcCannFinThis", "Esta tarefa ainda não pode terminar.n");
    mensagens.put("facades.EnactmentEngine.UserActvHasPend", "A atividade possui dependências.");
    mensagens.put("facades.EnactmentEngine.DBExcActvDoesNot", "Atividade não existe.");
    mensagens.put(
        "facades.EnactmentEngine.UserExcCannFinThisTask", "Esta tarefa ainda não pode terminar.n");
    mensagens.put("facades.EnactmentEngine.UserExcCanNotBeDel", "' não pode ser delegada");
    mensagens.put("facades.EnactmentEngine.UserExcIsAlreFail", " já está falhada.");
    mensagens.put("facades.EnactmentEngine.UserExcIsAlreCancel", " já está cancelada.");
    mensagens.put(
        "facades.EnactmentEngine.UserExcNotPossibleToCanc",
        "Não é possível cancelar atividades neste estado ");
    mensagens.put(
        "facades.EnactmentEngine.DBExcDAtabaseAccResource",
        "Acesso à base de dados para o recurso ");
    mensagens.put("facades.EnactmentEngine.DBExcResource", "Recurso ");
    mensagens.put("facades.EnactmentEngine.UserExcResource", "Recurso ");
    mensagens.put("facades.EnactmentEngine.UserExcIsAlreUnavail", " está indisponível.");
    mensagens.put(
        "facades.EnactmentEngine.UserExcThisOperatCanBe",
        "Esta operação pode ser aplicada somente sobre recursos compartilhados.");
    mensagens.put("facades.EnactmentEngine.UserExcIsAlreAvaila", " já está disponível.");
    mensagens.put("facades.EnactmentEngine.UserExcDefectInResour", "Defeito no recurso ");
    mensagens.put("facades.EnactmentEngine.UserExcAlreRegistered", " já registrado.");
    mensagens.put(
        "facades.EnactmentEngine.DBExcAAccToTheDatabase", "Erro durante acesso à Base de Dados.");
    mensagens.put("facades.EnactmentEngine.UserExcAgent", "Agente  ");
    mensagens.put(
        "facades.EnactmentEngine.UserExcIsAlrAlloc", " já está alocado para a atividade. ");
  }

  public static String getString(String key) {
    //		try {
    //			return RESOURCE_BUNDLE.getString(key);
    //		} catch (MissingResourceException e) {
    //			return '!' + key + '!';
    //		}
    return mensagens.get(key);
  }
}
