package com.zheng.liuju.bean;

import java.util.List;

public class CustomerBean {

    /**
     * code : 1
     * msg : success
     * data : {"id":"540455292-S1qOZ3RdX8rsOP","email":"huzhihong123@gmail.com","firstName":null,"lastName":null,"phone":{"areaCode":"57","number":"3115424071"},"identification":{"type":"DNI","number":"694852609962"},"defaultAddress":null,"address":{"id":null,"zipCode":null,"streetName":null,"streetNumber":null},"dateRegistered":null,"description":null,"dateCreated":"2020-04-07 12:00:00","dateLastUpdated":"2020-04-07 12:00:00","metadata":null,"defaultCard":"1586249540353","cards":[{"idempotenceKey":null,"lastApiResponse":null,"marketplaceAccessToken":null,"id":"1586249540353","customerId":"540455292-S1qOZ3RdX8rsOP","expirationMonth":11,"expirationYear":2025,"firstSixDigits":"503175","lastFourDigits":"0604","paymentMethod":{"id":"master","name":"master","paymentTypeId":"credit_card","thumbnail":"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif","secureThumbnail":"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif"},"securityCode":{"length":3,"cardLocation":"back"},"issuer":{"id":"204","name":"Mastercard"},"cardholder":{"name":"APRO HAO","identification":{"number":"58464656655","subtype":null,"type":"CC"}},"dateCreated":"2020-04-07 16:51:16","dateLastUpdated":"2020-04-07 16:51:16","paymentMethodId":null}],"addresses":[],"liveMode":null}
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
        /**
         * id : 540455292-S1qOZ3RdX8rsOP
         * email : huzhihong123@gmail.com
         * firstName : null
         * lastName : null
         * phone : {"areaCode":"57","number":"3115424071"}
         * identification : {"type":"DNI","number":"694852609962"}
         * defaultAddress : null
         * address : {"id":null,"zipCode":null,"streetName":null,"streetNumber":null}
         * dateRegistered : null
         * description : null
         * dateCreated : 2020-04-07 12:00:00
         * dateLastUpdated : 2020-04-07 12:00:00
         * metadata : null
         * defaultCard : 1586249540353
         * cards : [{"idempotenceKey":null,"lastApiResponse":null,"marketplaceAccessToken":null,"id":"1586249540353","customerId":"540455292-S1qOZ3RdX8rsOP","expirationMonth":11,"expirationYear":2025,"firstSixDigits":"503175","lastFourDigits":"0604","paymentMethod":{"id":"master","name":"master","paymentTypeId":"credit_card","thumbnail":"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif","secureThumbnail":"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif"},"securityCode":{"length":3,"cardLocation":"back"},"issuer":{"id":"204","name":"Mastercard"},"cardholder":{"name":"APRO HAO","identification":{"number":"58464656655","subtype":null,"type":"CC"}},"dateCreated":"2020-04-07 16:51:16","dateLastUpdated":"2020-04-07 16:51:16","paymentMethodId":null}]
         * addresses : []
         * liveMode : null
         */

        private String id;
        private String email;
        private Object firstName;
        private Object lastName;
        private PhoneBean phone;
        private IdentificationBean identification;
        private Object defaultAddress;
        private AddressBean address;
        private Object dateRegistered;
        private Object description;
        private String dateCreated;
        private String dateLastUpdated;
        private Object metadata;
        private String defaultCard;
        private Object liveMode;
        private List<CardsBean> cards;
        private List<?> addresses;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getFirstName() {
            return firstName;
        }

        public void setFirstName(Object firstName) {
            this.firstName = firstName;
        }

        public Object getLastName() {
            return lastName;
        }

        public void setLastName(Object lastName) {
            this.lastName = lastName;
        }

        public PhoneBean getPhone() {
            return phone;
        }

        public void setPhone(PhoneBean phone) {
            this.phone = phone;
        }

        public IdentificationBean getIdentification() {
            return identification;
        }

        public void setIdentification(IdentificationBean identification) {
            this.identification = identification;
        }

        public Object getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(Object defaultAddress) {
            this.defaultAddress = defaultAddress;
        }

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public Object getDateRegistered() {
            return dateRegistered;
        }

        public void setDateRegistered(Object dateRegistered) {
            this.dateRegistered = dateRegistered;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
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

        public Object getMetadata() {
            return metadata;
        }

        public void setMetadata(Object metadata) {
            this.metadata = metadata;
        }

        public String getDefaultCard() {
            return defaultCard;
        }

        public void setDefaultCard(String defaultCard) {
            this.defaultCard = defaultCard;
        }

        public Object getLiveMode() {
            return liveMode;
        }

        public void setLiveMode(Object liveMode) {
            this.liveMode = liveMode;
        }

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public List<?> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<?> addresses) {
            this.addresses = addresses;
        }

        public static class PhoneBean {
            /**
             * areaCode : 57
             * number : 3115424071
             */

            private String areaCode;
            private String number;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

        public static class IdentificationBean {
            /**
             * type : DNI
             * number : 694852609962
             */

            private String type;
            private String number;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

        public static class AddressBean {
            /**
             * id : null
             * zipCode : null
             * streetName : null
             * streetNumber : null
             */

            private Object id;
            private Object zipCode;
            private Object streetName;
            private Object streetNumber;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public Object getZipCode() {
                return zipCode;
            }

            public void setZipCode(Object zipCode) {
                this.zipCode = zipCode;
            }

            public Object getStreetName() {
                return streetName;
            }

            public void setStreetName(Object streetName) {
                this.streetName = streetName;
            }

            public Object getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(Object streetNumber) {
                this.streetNumber = streetNumber;
            }
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
                private IdentificationBeanX identification;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public IdentificationBeanX getIdentification() {
                    return identification;
                }

                public void setIdentification(IdentificationBeanX identification) {
                    this.identification = identification;
                }

                public static class IdentificationBeanX {
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
