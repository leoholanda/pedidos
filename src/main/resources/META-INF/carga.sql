-- Registra os modulos --

INSERT INTO public.modulo(id, descricao, nome) VALUES (1, 'Unidade', 'UNIDADE'),(2, 'Usuário', 'USUARIO'),(3, 'Produto', 'PRODUTO'),(4, 'Perfil', 'PERFIL'),(5, 'Fazer Pedido', 'FAZER_PEDIDO'),(6, 'Autorizar Pedido', 'AUTORIZAR_PEDIDO'),(7, 'Consultar Pedido', 'CONSULTAR_PEDIDO'),(8, 'Entregar Pedido', 'ENTREGAR_PEDIDO');

-- Registra perfil
INSERT INTO public.perfil(id, nome, status, data_atualizacao, data_criacao) VALUES (1, 'Administrador', 'ATIVADO', 'now', 'now'),(2, 'Bons Amigos', 'ATIVADO', 'now', 'now'),(3, 'Beta Clean', 'ATIVADO', 'now', 'now');

-- Registra o perfil_modulo

INSERT INTO public.perfil_modulo(perfil, modulo) VALUES (1, 1),(1, 2),(1, 3),(1, 4),(1, 5),(1, 6),(1, 7),(1, 8),(2, 1),(2, 3),(2, 8),(3, 2),(3, 4),(3, 5),(3, 6),(3, 7);

-- Registra Usuário

INSERT INTO public.usuario(id, cpf, email, nome, senha, sobrenome, status, data_atualizacao, data_criacao, perfil_id) VALUES (1, '92974139272', null, 'LEONARDO', '202cb962ac59075b964b07152d234b70', 'HOLANDA', 'AUTORIZADO', 'now', 'now', 1),(2, '74650017220', null, 'NAIEF', '202cb962ac59075b964b07152d234b70', 'SOBRENOME', 'AUTORIZADO', 'now','now', 2);