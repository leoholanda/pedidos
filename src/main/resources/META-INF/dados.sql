-- Registra os modulos --
INSERT INTO public.modulo(id, descricao, nome) VALUES (1, 'Unidade', 'UNIDADE'),(2, 'Usuario', 'USUARIO'),(3, 'Produto', 'PRODUTO'),(4, 'Perfil', 'PERFIL'),(5, 'Fazer Pedido', 'FAZER_PEDIDO'),(6, 'Autorizar Pedido', 'AUTORIZAR_PEDIDO'),(7, 'Consultar Pedido', 'CONSULTAR_PEDIDO'),(8, 'Entregar Pedido', 'ENTREGAR_PEDIDO');

-- Registra perfil
INSERT INTO public.perfil(id, nome, status, data_atualizacao, data_criacao) VALUES (1, 'Administrador', 'ATIVADO', 'now', 'now'),(2, 'Bons Amigos', 'ATIVADO', 'now', 'now'),(3, 'Beta Clean', 'ATIVADO', 'now', 'now');

-- Registra o perfil_modulo
INSERT INTO public.perfil_modulo(perfil, modulo) VALUES (1, 1),(1, 2),(1, 3),(1, 4),(1, 5),(2, 1),(2, 3),(2, 7),(2, 8),(3,3);

-- Registra Usuário
INSERT INTO public.usuario(id, cpf, email, nome, senha, sobrenome, status, data_atualizacao, data_criacao, perfil_id, area) VALUES (1, 'admin', null, 'ADMINISTRADOR', '202cb962ac59075b964b07152d234b70', 'DO SISTEMA', 'AUTORIZADO', 'now','now', 1, 'SAUDE'),(2, 'bons-amigos', null, 'BONS', '202cb962ac59075b964b07152d234b70', 'AMIGOS', 'AUTORIZADO', 'now', 'now', 2, 'EDUCACAO'),(3, 'beta-clean', null, 'BETA', '202cb962ac59075b964b07152d234b70', 'CLEAN', 'AUTORIZADO', 'now', 'now', 3, 'SAUDE');