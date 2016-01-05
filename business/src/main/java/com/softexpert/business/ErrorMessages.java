package com.softexpert.business;

public interface ErrorMessages {

	static final String DELETE_ERROR = "Ops... registro não pode ser removido ou está send utilizado.";
	static final String COULD_NOT_EDIT_ERROR = "Ops... registro não pode ser editado, verifique se todos os dados então preenchidos corretamente.";
	static final String SAVE_ERROR = "Ops... erro ao salvar, verifique se todos os dados então preenchidos corretamente :(";
	static final String SEARCH_ERROR = "Ops... ocorreu erro ao buscas, registro pode não existir mais.";
}
