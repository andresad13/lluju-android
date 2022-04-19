package com.zheng.liuju.bean;

import java.util.List;

public class CardList {


    /**
     * code : 1
     * msg : success
     * data : {"cards":[{"idempotenceKey":null,"lastApiResponse":null,"marketplaceAccessToken":null,"id":"1586249540353","customerId":"540455292-S1qOZ3RdX8rsOP","expirationMonth":11,"expirationYear":2025,"firstSixDigits":"503175","lastFourDigits":"0604","paymentMethod":{"id":"master","name":"master","paymentTypeId":"credit_card","thumbnail":"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif","secureThumbnail":"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif"},"securityCode":{"length":3,"cardLocation":"back"},"issuer":{"id":"204","name":"Mastercard"},"cardholder":{"name":"APRO HAO","identification":{"number":"58464656655","subtype":null,"type":"CC"}},"dateCreated":"2020-04-07 16:51:16","dateLastUpdated":"2020-04-07 16:51:16","paymentMethodId":null}]}
     * total : 1
     */

    private int code;
    private String msg;
    private DataBean data;
    private int total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class DataBean {
        private List<CardsBean> cards;

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public static class CardsBean {
            /**
             * idempotenceKey : null
             * lastApiResponse : null
             * marketplaceAccessToken : null
             * id : 1586249540353
             * customerId : 540455292-S1qOZ3RdX8rsOP
             * expirationMonth : 11
             * expirationYear : 2025
             * firstSixDigits : 503175
             * lastFourDigits : 0604
             * paymentMethod : {"id":"master","name":"master","paymentTypeId":"credit_card","thumbnail":"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif","secureThumbnail":"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif"}
             * securityCode : {"length":3,"cardLocation":"back"}
             * issuer : {"id":"204","name":"Mastercard"}
             * cardholder : {"name":"APRO HAO","identification":{"number":"58464656655","subtype":null,"type":"CC"}}
             * dateCreated : 2020-04-07 16:51:16
             * dateLastUpdated : 2020-04-07 16:51:16
             * paymentMethodId : null
             */

            private Object idempotenceKey;
            private Object lastApiResponse;
            private Object marketplaceAccessToken;
            private String id;
            private String customerId;
            private int expirationMonth;
            private int expirationYear;
            private String firstSixDigits;
            private String lastFourDigits;
            private PaymentMethodBean paymentMethod;
            private SecurityCodeBean securityCode;
            private IssuerBean issuer;
            private CardholderBean cardholder;
            private String dateCreated;
            private String dateLastUpdated;
            private Object paymentMethodId;

            public Object getIdempotenceKey() {
                return idempotenceKey;
            }

            public void setIdempotenceKey(Object idempotenceKey) {
                this.idempotenceKey = idempotenceKey;
            }

            public Object getLastApiResponse() {
                return lastApiResponse;
            }

            public void setLastApiResponse(Object lastApiResponse) {
                this.lastApiResponse = lastApiResponse;
            }

            public Object getMarketplaceAccessToken() {
                return marketplaceAccessToken;
            }

            public void setMarketplaceAccessToken(Object marketplaceAccessToken) {
                this.marketplaceAccessToken = marketplaceAccessToken;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public int getExpirationMonth() {
                return expirationMonth;
            }

            public void setExpirationMonth(int expirationMonth) {
                this.expirationMonth = expirationMonth;
            }

            public int getExpirationYear() {
                return expirationYear;
            }

            public void setExpirationYear(int expirationYear) {
                this.expirationYear = expirationYear;
            }

            public String getFirstSixDigits() {
                return firstSixDigits;
            }

            public void setFirstSixDigits(String firstSixDigits) {
                this.firstSixDigits = firstSixDigits;
            }

            public String getLastFourDigits() {
                return lastFourDigits;
            }

            public void setLastFourDigits(String lastFourDigits) {
                this.lastFourDigits = lastFourDigits;
            }

            public PaymentMethodBean getPaymentMethod() {
                return paymentMethod;
            }

            public void setPaymentMethod(PaymentMethodBean paymentMethod) {
                this.paymentMethod = paymentMethod;
            }

            public SecurityCodeBean getSecurityCode() {
                return securityCode;
            }

            public void setSecurityCode(SecurityCodeBean securityCode) {
                this.securityCode = securityCode;
            }

            public IssuerBean getIssuer() {
                return issuer;
            }

            public void setIssuer(IssuerBean issuer) {
                this.issuer = issuer;
            }

            public CardholderBean getCardholder() {
                return cardholder;
            }

            public void setCardholder(CardholderBean cardholder) {
                this.cardholder = cardholder;
            }

            public String getDateCreated() {
                return dateCreated;
            }

            public void setDateCreated(String dateCreated) {
                this.dateCreated = dateCreated;
            }

            public String getDateLastUpdated() {
                return dateLastUpdated;
            }

            public void setDateLastUpdated(String dateLastUpdated) {
                this.dateLastUpdated = dateLastUpdated;
            }

            public Object getPaymentMethodId() {
                return paymentMethodId;
            }

            public void setPaymentMethodId(Object paymentMethodId) {
                this.paymentMethodId = paymentMethodId;
            }

            public static class PaymentMethodBean {
                /**
                 * id : master
                 * name : master
                 * paymentTypeId : credit_card
                 * thumbnail : http://img.mlstatic.com/org-img/MP3/API/logos/master.gif
                 * secureThumbnail : https://www.mercadopago.com/org-img/MP3/API/logos/master.gif
                 */

                private String id;
                private String name;
                private String paymentTypeId;
                private String thumbnail;
                private String secureThumbnail;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPaymentTypeId() {
                    return paymentTypeId;
                }

                public void setPaymentTypeId(String paymentTypeId) {
                    this.paymentTypeId = paymentTypeId;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }

                public String getSecureThumbnail() {
                    return secureThumbnail;
                }

                public void setSecureThumbnail(String secureThumbnail) {
                    this.secureThumbnail = secureThumbnail;
                }
            }

            public static class SecurityCodeBean {
                /**
                 * length : 3
                 * cardLocation : back
                 */

                private int length;
                private String cardLocation;

                public int getLength() {
                    return length;
                }

                public void setLength(int length) {
                    this.length = length;
                }

                public String getCardLocation() {
                    return cardLocation;
                }

                public void setCardLocation(String cardLocation) {
                    this.cardLocation = cardLocation;
                }
            }

            public static class IssuerBean {
                /**
                 * id : 204
                 * name : Mastercard
                 */

                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class CardholderBean {
                /**
                 * name : APRO HAO
                 * identification : {"number":"58464656655","subtype":null,"type":"CC"}
                 */

                private String name;
                private IdentificationBean identification;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public IdentificationBean getIdentification() {
                    return identification;
                }

                public void setIdentification(IdentificationBean identification) {
                    this.identification = identification;
                }

                public static class IdentificationBean {
                    /**
                     * number : 58464656655
                     * subtype : null
                     * type : CC
                     */

                    private String number;
                    private Object subtype;
                    private String type;

                    public String getNumber() {
                        return number;
                    }

                    public void setNumber(String number) {
                        this.number = number;
                    }

                    public Object getSubtype() {
                        return subtype;
                    }

                    public void setSubtype(Object subtype) {
                        this.subtype = subtype;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }
            }
        }
    }
}
