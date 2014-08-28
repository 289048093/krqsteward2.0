/*
Navicat MySQL Data Transfer

Source Server         : jconncg4erzzs.mysql.rds.aliyuncs.com

Source Server Version : 50161
Source Host           : jconncg4erzzs.mysql.rds.aliyuncs.com
:3306
Source Database       : steward

Target Server Type    : MYSQL
Target Server Version : 50161
File Encoding         : 65001

Date: 2014-06-24 10:32:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_repository`
-- ----------------------------
DROP TABLE IF EXISTS `t_repository`;
CREATE TABLE `t_repository` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '仓库名',
  `code` varchar(16) DEFAULT NULL COMMENT '仓库编码',
  `address` varchar(200) DEFAULT NULL COMMENT '仓库地址',
  `charge_person_id` int(11) DEFAULT NULL COMMENT '责任人id',
  `shipping_comp` varchar(20) DEFAULT NULL COMMENT '物流公司',
  `charge_mobile` varchar(20) DEFAULT NULL COMMENT '责任人手机号',
  `charge_phone` varchar(20) DEFAULT NULL COMMENT '负责人电话',
  `create_time` datetime DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime DEFAULT '0000-00-00 00:00:00',
  `operator_id` int(11) DEFAULT NULL,
  `province_id` varchar(10) NOT NULL,
  `city_id` varchar(10) DEFAULT NULL,
  `area_id` varchar(10) DEFAULT NULL,
  `column_15` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_repository
-- ----------------------------
INSERT INTO `t_repository` VALUES ('1', '唐采潮州仓', 'tcczc', '广东省潮州市枫溪区潮汕公路槐山岗管理区（老四通陶瓷旁）', '607', 'yunda', '13534687520', '', '2014-06-09 18:32:28', '2014-06-23 15:58:49', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('2', '金怡潮州仓', 'JYCZC', '广东省潮州市潮安县东凤镇东三红绵工业区金怡公司', '613', 'ems', '15875452848', '', '2014-06-09 18:32:35', '2014-06-23 15:59:17', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('3', '酷彩上海仓', 'CCSHC', '上海市华山路1336号玉嘉大厦15楼（好礼公司）', null, 'shunfeng', '13501672699', '', '2014-06-09 18:33:25', '2014-06-09 18:33:25', '414', '310000', null, null, null);
INSERT INTO `t_repository` VALUES ('4', '杰美思深圳仓', 'jmsszc', '广东省惠州市惠阳区新圩镇花果山工业区，杰美思仓库', '609', 'ems', '13682325380', '', '2014-06-09 18:34:06', '2014-06-23 16:00:04', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('5', '观澜仓', 'glc', '广东省江门市蓬江区蛇背里68号6-7卡', '627', 'ems', '13725982013', '', '2014-06-09 18:34:09', '2014-06-23 16:00:16', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('6', '威仕乐太原仓', 'WSLTYC', '山西省太原市尖草坪区钢园路73号不锈钢工业园A区2号，太原恒丰不锈钢有限公司', null, 'shunfeng', '13613476587', '', '2014-06-09 18:34:29', '2014-06-09 18:34:29', '414', '140000', null, null, null);
INSERT INTO `t_repository` VALUES ('7', '家乐美永康仓', 'JLMYKC', '浙江永康市经济开发区清源路13号', null, 'huitongkuaidi', '13506596903', '', '2014-06-09 18:35:01', '2014-06-09 18:35:01', '414', '330000', null, null, null);
INSERT INTO `t_repository` VALUES ('8', '毛公鼎北京仓', 'mgdbjc', '北京市 海澱區北四環西路9號銀谷大廈1707室', '618', 'huitongkuaidi', '13811583390', '', '2014-06-09 18:35:03', '2014-06-23 16:16:50', '414', '110000', null, null, null);
INSERT INTO `t_repository` VALUES ('9', '尚尼新兴仓', 'snxxc', '广东省肇庆市端州区玑西路西太和路东天汇置业内天宇肇庆仓', '604', 'zhongtong', '13435983295', '0766-2923801', '2014-06-09 18:35:12', '2014-06-23 16:17:03', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('10', '绿的广州仓', 'LDGZC', '广州市白云区钟落潭镇乌溪村一队竹料商贸城（广州市科韵家庭用品有限公司）', '615', 'shentong', '13426727394', '', '2014-06-09 18:35:49', '2014-06-23 16:17:17', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('11', 'TOMMASI', 'tommasi', ' 广东省江门市蓬江区潮连镇田园路39号', '626', 'ems', '13427189987', '', '2014-06-09 18:35:56', '2014-06-23 16:17:28', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('12', '美思工坊惠州仓', 'MSGFHZC', '广东省惠州市仲恺高新区平南工业区和畅西3路49号宏利五金', '616', 'yuantong', '18665296587', '', '2014-06-09 18:36:48', '2014-06-23 16:17:45', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('13', '露西娜揭阳仓', 'lxnjyc', '广东省揭阳市东山区乔东工业区华洋四楼', '628', 'huitongkuaidi', '13531996069', '', '2014-06-09 18:36:54', '2014-06-23 16:17:55', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('14', '喜尚阳惠州仓', 'xsyhzc', '广东省惠州市惠城区小金口镇青塘坑尾巷北63号', '610', 'zhongtong', '13680862251', '', '2014-06-09 18:37:13', '2014-06-23 16:18:06', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('15', '宜可江门仓', 'ykjmc', '广东江门新会今古洲经济开发区', '619', 'tiantian', '13660896060', '020-38909248-361', '2014-06-09 18:37:25', '2014-06-23 16:18:21', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('16', '芒果深圳仓', 'MGSZC', '广东省深圳市南山区南海大道新保辉大厦10楼EF    蔡丽琪（收）  13430584505', '617', 'yunda', '13828383093', '', '2014-06-09 18:37:30', '2014-06-23 16:19:56', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('17', '波米番禹仓', 'bmpyc', '广州市番禺区洛浦街沙溪村幸福北路二村东街51号', '629', 'yunda', '15917196624', '020-34502949', '2014-06-09 18:37:39', '2014-06-23 16:20:54', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('18', '戴德惠州仓', 'ddhzc', '广东惠州市仲恺高新区平南工业区和畅西3路28号（阜东五金）', '630', 'shentong', '', '', '2014-06-09 18:38:16', '2014-06-23 16:21:06', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('19', '柏欧煮意东莞仓', 'bozydgc', '广东省东莞市凤岗镇雁田管理区镇田西路北埔工业区东莞长城光学厂', '612', 'tiantian', '13416931859', '', '2014-06-09 18:38:44', '2014-06-23 16:21:16', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('20', '爱东爱西江门仓', 'adaxjmc', '广东省江门市蓬江区潮连镇田园路39号（新仓地址）', '606', 'yuantong', '13422766165', '0750-3729236', '2014-06-09 18:38:52', '2014-06-23 16:22:14', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('21', '25°广州仓', 'eswgzc', '广州市越秀区东风东路552号东风大酒店二楼218室', '620', 'yunda', '18929596095', '', '2014-06-09 18:39:38', '2014-06-23 16:22:26', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('22', '恩腾永康仓', 'etykc', '浙江省金华市永康市经济开发区科源路866号', '614', 'yunda', '15958466122', '', '2014-06-09 18:39:41', '2014-06-23 16:22:36', '414', '330000', null, null, null);
INSERT INTO `t_repository` VALUES ('23', '查蒙蒂纳杭州仓', 'cmdnhzc', '浙江省杭州市江干区九环路35号（巨星科技）', '605', 'shentong', '', '', '2014-06-09 18:39:43', '2014-06-23 16:22:45', '414', '330000', null, null, null);
INSERT INTO `t_repository` VALUES ('24', '礼德揭阳仓', 'ldjyc', '广东省揭阳市东山区仁义路南仁港商住楼C栋10-11号', '611', 'guotongkuaidi', '18022578133', '', '2014-06-09 18:40:40', '2014-06-23 16:22:57', '414', '440000', null, null, null);
INSERT INTO `t_repository` VALUES ('25', '兰亭紫苑宜兴仓', 'ltzyyxc', '江苏无锡市宜兴市', '621', 'yunda', '', '', '2014-06-09 18:40:51', '2014-06-23 16:23:06', '414', '320000', null, null, null);
INSERT INTO `t_repository` VALUES ('26', '捷尚居上海仓', 'jsjshc', '上海市宝山区南大路458号晶通物流园区4-3库位  岱博贸易', null, 'shunfeng', '', '', '2014-06-09 18:42:10', '2014-06-09 18:42:10', '414', '310000', null, null, null);
INSERT INTO `t_repository` VALUES ('27', '尚品厨北京仓', 'spcbjc', '北京市朝阳区工人体育场北路17号5层', '622', 'zhongtong', '18610678292', '', '2014-06-09 18:42:59', '2014-06-23 16:23:46', '414', '110000', null, null, null);
INSERT INTO `t_repository` VALUES ('28', '品之易肇庆仓', 'pzyzqc', '肇庆', '608', 'shunfeng', '', '', '2014-06-10 14:03:20', '2014-06-23 16:23:54', '414', '110000', null, null, null);
