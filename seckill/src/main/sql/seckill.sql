-- ��ɱִ�д洢����
DELIMITER $$ --   ; ת��Ϊ $$
-- ����洢����
-- ������in ��������� out �������
-- row_count()��������һ���޸�(delete,insert,update)����sql��Ӱ������
-- row_count()��0 δ�޸����ݣ�>0 ��ʾ�޸ĵ������� <0 sql����/δִ���޸�
create procedure `seckill`.`execute_seckill`
    (in v_seckill_id bigint, in v_phone bigint,
     in v_kill_time timestamp, out r_result int)
    begin
        declare insert_count int default 0;
        start transaction ;
        insert ignore into success_killed
            (seckill_id, user_phone, create_time)
            values (v_seckill_id, v_phone, v_kill_time);
        select row_count() into insert_count;
        if(insert_count = 0) then
            rollback ;
            set r_result = -1;
        elseif(insert_count < 0) then
            rollback ;
            set r_result = -2;
        else
            update seckill
            set number = number - 1
            where seckill_id = v_seckill_id
                and end_time > v_kill_time
                and start_time < v_kill_time
                and number > 0;
            select row_count() into insert_count;
            if(insert_count = 0) then
                rollback;
                set r_result = 0;
            elseif(insert_count < 0) then
                rollback;
                set r_result = -2;
            else
                commit ;
                set r_result = 1;
            end if;
        end if;
    end;
$$
-- �洢���̶������

DELIMITER ;
set @r_result = -3;
-- ִ�д洢����
call execute_seckill (1003, 18629567777, now(), @r_result);

-- ��ȡ���
select @r_result;