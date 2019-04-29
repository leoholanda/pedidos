package net.bonsamigos.util;

import javax.inject.Inject;

import net.bonsamigos.service.UnidadeService;

public class Paginacao {

	@Inject
	private UnidadeService unidadeService;

	private Integer first = 1;
	private Integer row = 10;

	public Integer getTotal() throws Exception {
		Integer total = unidadeService.findAll().size();
		return total;
	}

	public Integer getLastItem() throws Exception {
		int count = first + (row - 1);
		if (count > getTotal()) {
			count = getTotal();
		}
		return count;
	}

	public String proxima() throws Exception {
		first += row;

		if (first > getTotal())
			first -= row;

		return "list";
	}

	public String anterior() {
		first -= row;
		if (first < 0) {
			first = 1;
		}
		return "list";
	}

	public String primeiro() {
		first = 1;
		return "list";
	}

	public String ultima() throws Exception {
		int tot = getTotal() / row;
		int ret = getTotal() % row;
		if (ret > 5) {
			tot += 1;
		}
		first += row * tot;

		if (first > getTotal())
			first -= row * tot;

		return "list";
	}

}
