package it.localhost.trafficdroid.common;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import it.localhost.trafficdroid.R;
import android.content.Intent;
import android.util.SparseIntArray;

public class Const {
	public static final String STARTDIR_ELEMENT = "startdir";
	public static final String ENDDIR_ELEMENT = "enddir";
	public static final String SECTOR_ELEMENT = "sector";
	public static final String LABEL_ELEMENT = "label";
	public static final String KM_ELEMENT = "km";
	public static final String DIRA_ELEMENT = "dirA";
	public static final String DIRB_ELEMENT = "dirB";
	public static final String openRound = " (";
	public static final String closeRound = ")";
	public static final String separator = "; ";
	public static final String http = "http://";
	public static final String slash = "/";
	public static final String xml = ".xml";
	public static final String webcamFirst = "/autostrade-mobile/popupTelecamera.do?ua=Android%201.1&tlc=";
	public static final String webcamSecond = "/webcam/temp-imgs/camsbig/";
	public static final String events = "/portale/rss?rsstype=traffic";
	public static final String fuel = "/public/tabellanazionale/tabellaNazionale.php";
	public static final String tdData = "trafficData";
	public static final String jpg = ".jpg";
	public static final String beginUpdate = "it.localhost.trafficdroid.BEGIN_UPDATE";
	public static final String endUpdate = "it.localhost.trafficdroid.END_UPDATE";
	public static final String anlyticsId = "UA-12243941-5";
	public static final String item = "item";
	public static final String eventCatApp = "App";
	public static final String eventActionVersion = "Version";
	public static final String eventCatWebcam = "Webcam";
	public static final String eventActionRequest = "Request";
	public static final String eventActionOpen = "Open";
	public static final String eventActionNone = "None";
	public static final String exceptionCheck = "exceptionCheck";
	public static final String exceptionMsg = "exceptionMsg";
	public static final String url = "url";
	public static final String notFound = "NotFound";
	public static final String blank = " ";
	public static final String badNews = "Bad News: ";
	public static final String badNewsDelim = "\n";
	public static final String badNewsStreetDelim = " -";
	public static final String expanded = "Expanded";
	public static final String unknowError = "Unknow Error";
	public static final String badConf = "Configurazione errata: ";
	public static final String badTrafficProvider = "Provider Traffico Errato";
	public static final String noDataSpeed = "-";
	public static final String autoveloxLeft = "L";
	public static final String autoveloxRight = "R";
	public static final String autoveloxAll = "A";
	public static final String autoveloxNone = "0";
	public static final String autovelox = "Autovelox";
	public static final String removePrefToast = " è stato rimosso dai preferiti.";
	public static final String removePrefToastUndo = " è stato aggiunto ai preferiti.";
	public static final String disconnectedMessage = "Connessione di rete inesistente";
	public static final String bn_acc = "incidente";
	public static final String bn_anh = "animali";
	public static final String bn_bkd = "veicolo fermo o avaria";
	public static final String bn_fod = "nebbia";
	public static final String bn_fop = "nebbia a banchi";
	public static final String bn_ibu = "nevischio";
	public static final String bn_los1 = "code";
	public static final String bn_los2 = "traffico";
	public static final String bn_ocm = "perdita di carico";
	public static final String bn_peo = "pedoni";
	public static final String bn_pra = "pioggia";
	public static final String bn_pss = "personale su strada";
	public static final String bn_res = "chius";
	public static final String bn_rsr = "riduzione di carreggiata";
	public static final String bn_sab = "area di servizio";
	public static final String bn_sdc = "scambio di carreggiata";
	public static final String bn_sm = "catene";
	public static final String bn_sn = "mezzi spargisale";
	public static final String bn_sne = "neve";
	public static final String bn_spc = "controllo velocità";
	public static final String bn_win = "vento forte";
	public static final String paypal = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=F2QEX3LXPH776";
	public static final SimpleDateFormat sdfBnParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static final SimpleDateFormat sdfBnFormat = new SimpleDateFormat("H:m");
	public static final Intent beginUpdateIntent = new Intent(Const.beginUpdate);
	public static final Intent endUpdateIntent = new Intent(Const.endUpdate);
	public static final char charAutostrade = 'A';
	public static final char webcamTrueFirst = 'A';
	public static final char webcamTrueSecond = 'C';
	public static final char webcamNone = 'H';
	public static final int notificationId = 1;
	public static final int date = new GregorianCalendar().get(GregorianCalendar.DATE);
	public static final byte[] itemTypes = new byte[] { 0, 1 };
	public static final int[] colorCat = new int[] { 0xffffffff, 0xffff0000, 0xffff0000, 0xffff8000, 0xffffff00, 0xff47ffff, 0xff00ff00 };
	public static final SparseIntArray zonesResId = new SparseIntArray(64) {
		{
			put(1, R.array.zones1Id);
			put(2, R.array.zones2Id);
			put(3, R.array.zones3Id);
			put(4, R.array.zones4Id);
			put(5, R.array.zones5Id);
			put(6, R.array.zones6Id);
			put(7, R.array.zones7Id);
			put(8, R.array.zones8Id);
			put(9, R.array.zones9Id);
			put(10, R.array.zones10Id);
			put(11, R.array.zones11Id);
			put(12, R.array.zones12Id);
			put(13, R.array.zones13Id);
			put(14, R.array.zones14Id);
			put(15, R.array.zones15Id);
			put(16, R.array.zones16Id);
			put(18, R.array.zones18Id);
			put(19, R.array.zones19Id);
			put(20, R.array.zones20Id);
			put(21, R.array.zones21Id);
			put(22, R.array.zones22Id);
			put(23, R.array.zones23Id);
			put(24, R.array.zones24Id);
			put(25, R.array.zones25Id);
			put(26, R.array.zones26Id);
			put(27, R.array.zones27Id);
			put(28, R.array.zones28Id);
			put(29, R.array.zones29Id);
			put(30, R.array.zones30Id);
			put(31, R.array.zones31Id);
			put(32, R.array.zones32Id);
			put(50, R.array.zones50Id);
			put(51, R.array.zones51Id);
			put(52, R.array.zones52Id);
			put(55, R.array.zones55Id);
			put(56, R.array.zones56Id);
			put(90, R.array.zones90Id);
			put(91, R.array.zones91Id);
			put(101, R.array.zones101Id);
			put(102, R.array.zones102Id);
			put(103, R.array.zones103Id);
			put(111, R.array.zones111Id);
			put(121, R.array.zones121Id);
			put(131, R.array.zones131Id);
			put(141, R.array.zones141Id);
			put(142, R.array.zones142Id);
			put(143, R.array.zones143Id);
			put(144, R.array.zones144Id);
			put(161, R.array.zones161Id);
			put(181, R.array.zones181Id);
			put(211, R.array.zones211Id);
			put(241, R.array.zones241Id);
			put(261, R.array.zones261Id);
			put(262, R.array.zones262Id);
			put(263, R.array.zones263Id);
			put(291, R.array.zones291Id);
			put(301, R.array.zones301Id);
			put(302, R.array.zones302Id);
			put(303, R.array.zones303Id);
			put(501, R.array.zones501Id);
			put(551, R.array.zones551Id);
			put(552, R.array.zones552Id);
			put(553, R.array.zones553Id);
			put(701, R.array.zones701Id);
		}
	};
	public static final SparseIntArray zonesResName = new SparseIntArray(64) {
		{
			put(1, R.array.zones1Name);
			put(2, R.array.zones2Name);
			put(3, R.array.zones3Name);
			put(4, R.array.zones4Name);
			put(5, R.array.zones5Name);
			put(6, R.array.zones6Name);
			put(7, R.array.zones7Name);
			put(8, R.array.zones8Name);
			put(9, R.array.zones9Name);
			put(10, R.array.zones10Name);
			put(11, R.array.zones11Name);
			put(12, R.array.zones12Name);
			put(13, R.array.zones13Name);
			put(14, R.array.zones14Name);
			put(15, R.array.zones15Name);
			put(16, R.array.zones16Name);
			put(18, R.array.zones18Name);
			put(19, R.array.zones19Name);
			put(20, R.array.zones20Name);
			put(21, R.array.zones21Name);
			put(22, R.array.zones22Name);
			put(23, R.array.zones23Name);
			put(24, R.array.zones24Name);
			put(25, R.array.zones25Name);
			put(26, R.array.zones26Name);
			put(27, R.array.zones27Name);
			put(28, R.array.zones28Name);
			put(29, R.array.zones29Name);
			put(30, R.array.zones30Name);
			put(31, R.array.zones31Name);
			put(32, R.array.zones32Name);
			put(50, R.array.zones50Name);
			put(51, R.array.zones51Name);
			put(52, R.array.zones52Name);
			put(55, R.array.zones55Name);
			put(56, R.array.zones56Name);
			put(90, R.array.zones90Name);
			put(91, R.array.zones91Name);
			put(101, R.array.zones101Name);
			put(102, R.array.zones102Name);
			put(103, R.array.zones103Name);
			put(111, R.array.zones111Name);
			put(121, R.array.zones121Name);
			put(131, R.array.zones131Name);
			put(141, R.array.zones141Name);
			put(142, R.array.zones142Name);
			put(143, R.array.zones143Name);
			put(144, R.array.zones144Name);
			put(161, R.array.zones161Name);
			put(181, R.array.zones181Name);
			put(211, R.array.zones211Name);
			put(241, R.array.zones241Name);
			put(261, R.array.zones261Name);
			put(262, R.array.zones262Name);
			put(263, R.array.zones263Name);
			put(291, R.array.zones291Name);
			put(301, R.array.zones301Name);
			put(302, R.array.zones302Name);
			put(303, R.array.zones303Name);
			put(501, R.array.zones501Name);
			put(551, R.array.zones551Name);
			put(552, R.array.zones552Name);
			put(553, R.array.zones553Name);
			put(701, R.array.zones701Name);
		}
	};
	public static final SparseIntArray zonesResAutovelox = new SparseIntArray(64) {
		{
			put(1, R.array.zones1Autovelox);
			put(2, R.array.zones2Autovelox);
			put(3, R.array.zones3Autovelox);
			put(4, R.array.zones4Autovelox);
			put(5, R.array.zones5Autovelox);
			put(6, R.array.zones6Autovelox);
			put(7, R.array.zones7Autovelox);
			put(8, R.array.zones8Autovelox);
			put(9, R.array.zones9Autovelox);
			put(10, R.array.zones10Autovelox);
			put(11, R.array.zones11Autovelox);
			put(12, R.array.zones12Autovelox);
			put(13, R.array.zones13Autovelox);
			put(14, R.array.zones14Autovelox);
			put(15, R.array.zones15Autovelox);
			put(16, R.array.zones16Autovelox);
			put(18, R.array.zones18Autovelox);
			put(19, R.array.zones19Autovelox);
			put(20, R.array.zones20Autovelox);
			put(21, R.array.zones21Autovelox);
			put(22, R.array.zones22Autovelox);
			put(23, R.array.zones23Autovelox);
			put(24, R.array.zones24Autovelox);
			put(25, R.array.zones25Autovelox);
			put(26, R.array.zones26Autovelox);
			put(27, R.array.zones27Autovelox);
			put(28, R.array.zones28Autovelox);
			put(29, R.array.zones29Autovelox);
			put(30, R.array.zones30Autovelox);
			put(31, R.array.zones31Autovelox);
			put(32, R.array.zones32Autovelox);
			put(50, R.array.zones50Autovelox);
			put(51, R.array.zones51Autovelox);
			put(52, R.array.zones52Autovelox);
			put(55, R.array.zones55Autovelox);
			put(56, R.array.zones56Autovelox);
			put(90, R.array.zones90Autovelox);
			put(91, R.array.zones91Autovelox);
			put(101, R.array.zones101Autovelox);
			put(102, R.array.zones102Autovelox);
			put(103, R.array.zones103Autovelox);
			put(111, R.array.zones111Autovelox);
			put(121, R.array.zones121Autovelox);
			put(131, R.array.zones131Autovelox);
			put(141, R.array.zones141Autovelox);
			put(142, R.array.zones142Autovelox);
			put(143, R.array.zones143Autovelox);
			put(144, R.array.zones144Autovelox);
			put(161, R.array.zones161Autovelox);
			put(181, R.array.zones181Autovelox);
			put(211, R.array.zones211Autovelox);
			put(241, R.array.zones241Autovelox);
			put(261, R.array.zones261Autovelox);
			put(262, R.array.zones262Autovelox);
			put(263, R.array.zones263Autovelox);
			put(291, R.array.zones291Autovelox);
			put(301, R.array.zones301Autovelox);
			put(302, R.array.zones302Autovelox);
			put(303, R.array.zones303Autovelox);
			put(501, R.array.zones501Autovelox);
			put(551, R.array.zones551Autovelox);
			put(552, R.array.zones552Autovelox);
			put(553, R.array.zones553Autovelox);
			put(701, R.array.zones701Autovelox);
		}
	};
}