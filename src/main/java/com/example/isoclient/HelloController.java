package com.example.isoclient;

import com.example.DataSingleton;
import com.example.ISO8583.builders.ISOMessageBuilder;
import com.example.ISO8583.entities.ISOMessage;
import com.example.ISO8583.enums.FIELDS;
import com.example.ISO8583.enums.MESSAGE_FUNCTION;
import com.example.ISO8583.enums.MESSAGE_ORIGIN;
import com.example.ISO8583.enums.VERSION;
import com.example.ISO8583.exceptions.ISOException;
import com.example.ISO8583.utils.FixedBitSet;
import com.example.ISO8583.utils.StringUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Arrays;

public class HelloController {

    DataSingleton data = DataSingleton.getInstance();

    @FXML private javafx.scene.control.Button settings_button;
    @FXML private javafx.scene.control.TextArea request_text;
    @FXML private javafx.scene.control.TextArea response_text;
    private int length = 2;

    @FXML
    protected void onSendButtonClick() throws ISOException, InterruptedException {
        ISOMessage requestIsoMessage;
        String host = "127.0.0.1";
        int port = 8080;
        ByteSendingClient client = new ByteSendingClient(host, port, this);

        switch (data.getType_message()){
            case "Purchase Request":
                sendMessage(client, purchaseRequest());
                break;
            case "Network Key Change Request":
                sendMessage(client, networkKeyChangeRequest());
                break;
            case "Network MAC Key Change Request":
                sendMessage(client, networkMACKeyChangeRequest());
                break;
            case "Return Refund Request":
                sendMessage(client, returnRefundRequest());
                break;
            case "Return Reversal Request":
                sendMessage(client, returnReversalRequest());
                break;
            case "Purchase Reversal Request":
                sendMessage(client, purchaseReversalRequest());
                break;
            default:
                sendMessage(client, echoRequest());
        }


    }

    private void sendMessage(ByteSendingClient client, ISOMessage requestIsoMessage) throws InterruptedException {
        request_text.setText(textIsoMessage(requestIsoMessage).toString());
        client.connect(requestIsoMessage,data);

    }

    public void ResponseMessage(byte[] response) throws ISOException {
        ISOMessage responseIsoMessage = ISOMessageBuilder.Unpacker()
                .setMessage(Arrays.copyOfRange(response,2, response.length))
                .build();
        response_text.setText(textIsoMessage(responseIsoMessage).toString());
    }


    @FXML
    protected void onSettingsButtonClick() throws IOException {
        Stage stage = (Stage) settings_button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        stage.setTitle("Настройки");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 400, 350));
    }


    private static ISOMessage echoRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .networkManagement()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("990000")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102034")
                .setField(FIELDS.F11_STAN,  "1")
                .setField(FIELDS.F24_NII_FunctionCode, "831")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage networkKeyChangeRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .networkManagement()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("990000")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102034")
                .setField(FIELDS.F11_STAN,  "1")
                .setField(FIELDS.F24_NII_FunctionCode, "811")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setField(FIELDS.F64_MAC, "3E45B5667F2AFFB3")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage networkMACKeyChangeRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .networkManagement()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("990000")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102034")
                .setField(FIELDS.F11_STAN,  "1")
                .setField(FIELDS.F24_NII_FunctionCode, "815")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage purchaseRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .financial()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("000000")
                .setField(FIELDS.F2_PAN, "5423390007411264012")
                .setField(FIELDS.F4_AmountTransaction, "000000000100")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102042")
                .setField(FIELDS.F11_STAN, "000001")
                .setField(FIELDS.F12_LocalTime, "102034")
                .setField(FIELDS.F13_LocalDate, "0706")
                .setField(FIELDS.F22_EntryMode, "021")
                .setField(FIELDS.F24_NII_FunctionCode, "200")
                .setField(FIELDS.F25_POS_ConditionCode, "02")
                .setField(FIELDS.F35_Track2, "54233900074112640=991233000123410000")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setField(FIELDS.F48_AddData_Private, "0030044321013006119990150011")
                .setField(FIELDS.F49_CurrencyCode_Transaction, "810")
                .setField(FIELDS.F52_PIN, "01010101")
                .setField(FIELDS.F55_ICC, "b255")
                .setField(FIELDS.F64_MAC, "0101010101010101")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage returnRefundRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .financial()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("200000")
                .setField(FIELDS.F4_AmountTransaction, "000000000100")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102042")
                .setField(FIELDS.F11_STAN, "000001")
                .setField(FIELDS.F12_LocalTime, "102034")
                .setField(FIELDS.F13_LocalDate, "0706")
                .setField(FIELDS.F22_EntryMode, "021")
                .setField(FIELDS.F24_NII_FunctionCode, "200")
                .setField(FIELDS.F25_POS_ConditionCode, "02")
                .setField(FIELDS.F35_Track2, "54233900074112640=991233000123410000")
                .setField(FIELDS.F37_RRN, "000001007050")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setField(FIELDS.F49_CurrencyCode_Transaction, "810")
                .setField(FIELDS.F52_PIN, "01010101")
                .setField(FIELDS.F55_ICC, "b255")
                .setField(FIELDS.F64_MAC, "0101010101010101")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage purchaseReversalRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .reversal()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("002000")
                .setField(FIELDS.F4_AmountTransaction, "000000000100")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102042")
                .setField(FIELDS.F11_STAN, "000001")
                .setField(FIELDS.F12_LocalTime, "102034")
                .setField(FIELDS.F13_LocalDate, "0706")
                .setField(FIELDS.F22_EntryMode, "021")
                .setField(FIELDS.F24_NII_FunctionCode, "400")
                .setField(FIELDS.F25_POS_ConditionCode, "02")
                .setField(FIELDS.F35_Track2, "54233900074112640=991233000123410000")
                .setField(FIELDS.F37_RRN, "000001007050")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setField(FIELDS.F49_CurrencyCode_Transaction, "810")
                .setField(FIELDS.F55_ICC, "b255")
                .setField(FIELDS.F64_MAC, "0101010101010101")
                .setHeader("3038303082")
                .build();
    }

    private static ISOMessage returnReversalRequest() throws ISOException {
        return ISOMessageBuilder.Packer(VERSION.V1987)
                .reversal()
                .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                .processCode("202000")
                .setField(FIELDS.F4_AmountTransaction, "000000000100")
                .setField(FIELDS.F7_TransmissionDateTime, "0706102042")
                .setField(FIELDS.F11_STAN, "000001")
                .setField(FIELDS.F12_LocalTime, "102034")
                .setField(FIELDS.F13_LocalDate, "0706")
                .setField(FIELDS.F22_EntryMode, "021")
                .setField(FIELDS.F24_NII_FunctionCode, "400")
                .setField(FIELDS.F25_POS_ConditionCode, "02")
                .setField(FIELDS.F35_Track2, "54233900074112640=991233000123410000")
                .setField(FIELDS.F37_RRN, "000001007050")
                .setField(FIELDS.F41_CA_TerminalID, "00010001")
                .setField(FIELDS.F42_CA_ID, "123456789012345")
                .setField(FIELDS.F49_CurrencyCode_Transaction, "810")
                .setField(FIELDS.F55_ICC, "b255")
                .setField(FIELDS.F64_MAC, "0101010101010101")
                .setHeader("3038303082")
                .build();
    }

    private static StringBuilder textIsoMessage(ISOMessage isoMessage) {
        StringBuilder message_text = new StringBuilder();
        FixedBitSet pb = new FixedBitSet(64);
        pb.fromHexString(StringUtil.fromByteArray(isoMessage.getPrimaryBitmap()));
        int offset = 10;
        message_text.append("MTI: "+isoMessage.getMti()+"\n");
        message_text.append("Primary bitmap: "+StringUtil.fromByteArray(isoMessage.getPrimaryBitmap())+"\n");
        for (int o : pb.getIndexes()) {

            FIELDS field = FIELDS.valueOf(o);

            if (field.isFixed()) {
                int len = field.getLength();
                switch (field.getType()) {
                    case "n":
                        if (len % 2 != 0)
                            len++;
                        len = len / 2;
                        message_text.append(field.toString() +": "+ StringUtil.fromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + len))+"\n");
                        break;
                    default:
                        message_text.append(field.toString() +": "+ StringUtil.asciiFromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + len))+"\n");
                        break;
                }
                offset += len;
            } else {

                int formatLength = 1;
                switch (field.getFormat()) {
                    case "LL":
                        formatLength = 1;
                        break;
                    case "LLL":
                        formatLength = 2;
                        break;
                }

                int flen = Integer.valueOf(StringUtil.fromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + formatLength)));
                offset = offset + formatLength;
                switch (field.getType()) {
                    case "z":
                        message_text.append(field.toString() +": "+ StringUtil.asciiFromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + flen))+"\n");
                        break;
                    case "n":
                        if (flen % 2 != 0)
                            flen++;
                        flen /= 2;
                        message_text.append(field.toString() +": "+ StringUtil.fromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + flen))+"\n");
                        break;
                    default:
                        message_text.append(field.toString() +": "+ StringUtil.asciiFromByteArray(Arrays.copyOfRange(isoMessage.getBody(), offset, offset + flen))+"\n");
                        break;
                }



                offset += flen;
            }

        }
        return message_text;
    }
}