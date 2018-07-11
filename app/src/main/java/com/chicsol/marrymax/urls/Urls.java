package com.chicsol.marrymax.urls;

/*
import static com.chicsol.alfalah.webconfig.webconfig.ARDTOKENa;
import static com.chicsol.alfalah.webconfig.webconfig.PassPhraseArdApa;
import static com.chicsol.alfalah.webconfig.webconfig.baseUrla;*/


import static com.chicsol.marrymax.webconfig.webconfig.ARDTOKENa;
import static com.chicsol.marrymax.webconfig.webconfig.PassPhraseArdApa;
import static com.chicsol.marrymax.webconfig.webconfig.baseUrla;

/**
 * Created by Android on 12/14/2016.
 */

public class Urls {
    //change this url before deployment
    public static String baseUrl = baseUrla;
    public static String PassPhraseArdAp = PassPhraseArdApa;
    public static String ARDTOKEN = ARDTOKENa;


    //Registration Activity
    public static String LoginUrl = baseUrl + "/api/WebArd/Login";
    public static String reg_Listing = baseUrl + "/api/WebArd/GetRegisterListings";

    public static String RegistrationUrl = baseUrl + "/api/WebArd/Registration";

    //geography
    public static String RegisterGeography = baseUrl + "/api/WebArd/GetProfileData/";
    public static String setCountryChangeReason = baseUrl + "/api/WebArd/CountryChangeReason";

    public static String getStatesUrl = baseUrl + "/api/WebArd/GetStatesByCountryId/";
    public static String getStateCitiesUrl = baseUrl + "/api/WebArd/GetCitiesByStateId/";

    public static String updateUserGeographyUrl = baseUrl + "/api/WebArd/EditGeogrophics";

    //Appearance
    public static String RegGetProfileAppearance = baseUrl + "/api/WebArd/GetProfileAppearance/";
    public static String updateUserAppearanceUrl = baseUrl + "/api/WebArd/EditAppearance";


    //lifestyle
    public static String RegGetLifeStyle1Url = baseUrl + "/api/WebArd/GetProfileLifeStyle1/";
    public static String updateLifestyleUrl = baseUrl + "/api/WebArd/EditLifeStyle1";
    public static String RegGetLifeStyle1Url2 = baseUrl + "/api/WebArd/GetProfileLifeStyle2/";
    public static String updateLifestyleUrl2 = baseUrl + "/api/WebArd/EditLifeStyle2";
    public static String removeChildren = baseUrl + "/api/WebArd/RemoveChildren/";
    public static String removeSchool = baseUrl + "/api/WebArd/RemoveSchool/";


    //interest=====================================

    public static String regGetInterestUrl = baseUrl + "/api/WebArd/GetProfileInterest/";
    public static String updateInterestUrl = baseUrl + "/api/WebArd/EditInterest";

    //Perosnality  =====================================

    public static String getPersonalityUrl = baseUrl + "/api/WebArd/GetProfilePersonality/";
    public static String updatePersonalityUrl = baseUrl + "/api/WebArd/EditPersonality";

    public static String getPersonalityDosDonts = baseUrl + "/api/WebArd/GetPersonalityDosDonts";

    //get progress
    public static String getProgressbar = baseUrl + "/api/WebArd/GetProgressbar/";


    //Dashboard Main

    public static String getMembersListbyType = baseUrl + "/api/WebArd/MembersListbyType";
    public static String getDashboardData = baseUrl + "/api/WebArd/GetDashboardData";
    public static String getProfileCompletion = baseUrl + "/api/WebArd/GetProfileCompletion/";
    public static String getAdminNotes = baseUrl + "/api/WebArd/GetAdminNotes/";
    public static String getStatus = baseUrl + "/api/WebArd/GetStatus/";

    //UserProfile Activity
    public static String getProfileDetail = baseUrl + "/api/WebArd/ProfileDetail";
    public static String mobileInfo = baseUrl + "/api/WebArd/MobileInfo";

    // Notes
    public static String membersNotes = baseUrl + "/api/WebArd/MembersNotes";
    public static String sendNotes = baseUrl + "/api/WebArd/SendNotes";
    public static String deleteNotes = baseUrl + "/api/WebArd/DeleteNotes";

    // Saved Lists
    public static String myList = baseUrl + "/api/WebArd/MyList";
    public static String addEditList = baseUrl + "/api/WebArd/AddEditList";
    public static String deleteList = baseUrl + "/api/WebArd/DeleteList";
    public static String addMemList = baseUrl + "/api/WebArd/AddMemList";


    //Show Interest
    public static String interestProvisions = baseUrl + "/api/WebArd/InterestProvisions";
    public static String showInterest = baseUrl + "/api/WebArd/ShowInterest";

    //withdrawinterest
    public static String withdrawInterest = baseUrl + "/api/WebArd/WithdrawInterest";

    //response interest
    public static String responseInterest = baseUrl + "/api/WebArd/ResponseInterest";

    //block reportConcern
    public static String getBlockedList = baseUrl + "/api/WebArd/GetBlockedList/";

    public static String getBlockReasonData = baseUrl + "/api/WebArd/GetBlockReasonData";
    public static String blockReason = baseUrl + "/api/WebArd/BlockReason";
    public static String getReportConcernData = baseUrl + "/api/WebArd/GetReportConcernData";

    public static String reportConcern = baseUrl + "/api/WebArd/ReportConcern";

    // RemoveMember
    public static String getRemovedList = baseUrl + "/api/WebArd/GetRemovedList/";
    public static String removeMember = baseUrl + "/api/WebArd/RemoveMember";
    public static String removedList = baseUrl + "/api/WebArd/RemovedList";

    //AddRemoveFavorites
    public static String addRemoveFavorites = baseUrl + "/api/WebArd/AddRemoveFavorites";
    //match aid
    public static String getAssistance = baseUrl + "/api/WebArd/GetAssistance/";
    public static String addAssistance = baseUrl + "/api/WebArd/AddAssistance";


    //Search Data
    public static String getSaveSearchLists = baseUrl + "/api/WebArd/GetSaveSearchLists/";
    public static String getSearchLists = baseUrl + "/api/WebArd/GetSearchLists/";
    public static String getRawData = baseUrl + "/api/WebArd/GetRawData/";
    public static String getSrhRawData = baseUrl + "/api/WebArd/GetSrhRawData/";

    public static String getStates = baseUrl + "/api/WebArd/GetStates/";
    public static String saveSearch = baseUrl + "/api/WebArd/SaveSearch";

    public static String getCities = baseUrl + "/api/WebArd/GetCities/";

    //My Matches
    //public static String getSearchedProfile = baseUrl + "/api/WebArd/GetSearchedProfile  ";


    public static String searchProfiles = baseUrl + "/api/WebArd/SearchProfiles";//search profiles

    public static String listProfiles = baseUrl + "/api/WebArd/ListProfiles";//list profiles


    /*submit request for imageview phoneview */
    public static String submitRequest = baseUrl + "/api/WebArd/SubmitRequest ";

//Account Settingsss==============================

    public static String updatePassword = baseUrl + "/api/WebArd/UpdatePassword";
    public static String getProfileContact = baseUrl + "/api/WebArd/GetProfileContact/";
    public static String editContact = baseUrl + "/api/WebArd/EditContact";
    public static String deleteContact = baseUrl + "/api/WebArd/DeleteContact";
    public static String getMobileCode = baseUrl + "/api/WebArd/GetMobileCode/";
    public static String validateMobile = baseUrl + "/api/WebArd/ValidateMobile";
    public static String getPreferences = baseUrl + "/api/WebArd/GetPreferences/";
    public static String editPreferences = baseUrl + "/api/WebArd/EditPreferences";
    public static String getSubscriptions = baseUrl + "/api/WebArd/GetSubscriptions/";

    public static String subscriptionData = baseUrl + "/api/WebArd/SubscriptionData";
    public static String getNotificationList = baseUrl + "/api/WebArd/getNotificationList/";
    public static String changeNotification = baseUrl + "/api/WebArd/ChangeNotification";
    public static String changeNotificationall = baseUrl + "/api/WebArd/ChangeNotificationAll";
    public static String getProfilePrivacy = baseUrl + "/api/WebArd/GetProfilePrivacy/";
    public static String editPrivacy = baseUrl + "/api/WebArd/EditPrivacy";
    public static String getDeactivateReasons = baseUrl + "/api/WebArd/GetDeactivateReasons/";
    public static String accountDeactivate = baseUrl + "/api/WebArd/AccountDeactivate";
    public static String updateEmail = baseUrl + "/api/WebArd/UpdateEmail";


    //==INBOX======================================================
    public static String getMessagesList = baseUrl + "/api/WebArd/GetMessagesList/";
    public static String messageDetail = baseUrl + "/api/WebArd/MessageDetail";
    public static String sendMessage = baseUrl + "/api/WebArd/SendMessage";
    public static String interestRequestType = baseUrl + "/api/WebArd/InterestRequestType";
    //  public static String responseInterest = baseUrl + "/api/WebArd/ResponseInterest";
    public static String deleteMessages = baseUrl + "/api/WebArd/DeleteMessages";


    //=====================Notifications=========================
    public static String getNotificationCount = baseUrl + "/api/WebArd/GetNotificationCount/";
    public static String loadNotifications = baseUrl + "/api/WebArd/LoadNotifications";
    public static String loadArchive = baseUrl + "/api/WebArd/LoadArchive";

    //=====================MyList=========================
    public static String myContacts = baseUrl + "/api/WebArd/MyContacts/";
    public static String deleteMyContact = baseUrl + "/api/WebArd/DeleteMyContact";
    public static String getSavedList = baseUrl + "/api/WebArd/GetSavedList/";


    //========================PhotoUpload================================
    public static String fileUpload = baseUrl + "/api/FileUpd/UserPhoto";

    public static String getPhotoDosDonts = baseUrl + "/api/WebArd/GetPhotoDosDonts/";
    public static String getMembersPictures = baseUrl + "/api/WebArd/GetMembersPictures/";


    public static String defaultPicture = baseUrl + "/api/WebArd/DefaultPicture";
    public static String deletePics = baseUrl + "/api/WebArd/DeletePics";


    //==========================Saved Search List===============================
    public static String deleteSaveSearch = baseUrl + "/api/WebArd/DeleteSaveSearch";
    public static String updateSaveSearch = baseUrl + "/api/WebArd/UpdateSaveSearch";

    //Forget password
    public static String sendPassCode = baseUrl + "/api/WebArd/SendPassCode";

    //email verification
    public static String getEmailCode = baseUrl + "/api/WebArd/GetEmailCode/";

    //=============================================================================

    public static String subscriptionUrl = baseUrl + "/Subscription";

    public static String about = baseUrl + "/About";
    public static String matchMaking = baseUrl + "/MatchMaking";
    public static String faqs = baseUrl + "/Faqs";
    public static String benefits = baseUrl + "/MembershipPlans";


    //=============Request And Permissios==================================================
    public static String requestPermissions = baseUrl + "/api/WebArd/RequestPermissions";
    public static String grantPermission = baseUrl + "/api/WebArd/GrantPermission";
    public static String withdrawPermission = baseUrl + "/api/WebArd/WithdrawPermission";


    //===========MY RECOMMENDED MATCHES =========================
    public static String recommendList = baseUrl + "/api/WebArd/RecommendList";
    public static String recommendResponse = baseUrl + "/api/WebArd/RecommendResponse";


    //subscriptions
    public static String getPersonsList = baseUrl + "/api/WebArd/GetPersonsList";
    public static String getPaymentPending = baseUrl + "/api/WebArd/GetPaymentPending/";
    public static String generateCart = baseUrl + "/api/WebArd/GenerateCart";

    public static String getPromoCode = baseUrl + "/api/WebArd/GetPromoCode/";
    public static String packages = baseUrl + "/api/WebArd/Packages";


    public static String processPayment = baseUrl + "/api/WebArd/ProcessPayment";
    public static String printCart = baseUrl + "/api/WebArd/PrintCart";


    public static String forwardMember = baseUrl + "/api/WebArd/ForwardMember";


    //================MATCH AID==================
    public static String getAssistanceList = baseUrl + "/api/WebArd/GetAssistanceList/";
    public static String addFeedback = baseUrl + "/api/WebArd/AddFeedback";


    //================Member Saved List==================
    public static String membersList = baseUrl + "/api/WebArd/MembersList";
    public static String removeMemList = baseUrl + "/api/WebArd/RemoveMemList";


    //================Permissions==================
    public static String permission = baseUrl + "/api/WebArd/Permission/";

    //================Request profile change==================
    public static String getRequestCategory = baseUrl + "/api/WebArd/GetRequestCategory";
    public static String getRequestOptions = baseUrl + "/api/WebArd/GetRequestOptions/";
    public static String requestUpdate = baseUrl + "/api/WebArd/RequestUpdate";


    //===============Interest Request Count ==================
    public static String getCommunicationCount = baseUrl + "/api/WebArd/GetCommunicationCount/";


    //===============My Profile Status ==================
    public static String getPhn = baseUrl + "/api/WebArd/GetPhn/";


    //===============Questions ==================
    public static String getQuestionAnswers = baseUrl + "/api/WebArd/GetQuestionAnswers/";
    public static String getQuestionInbox = baseUrl + "/api/WebArd/GetQuestionInbox/";

    public static String questionDetails = baseUrl + "/api/WebArd/QuestionDetails";
    public static String sendQuestion = baseUrl + "/api/WebArd/SendQuestion";
    public static String sendAnswer = baseUrl + "/api/WebArd/SendAnswer";
    public static String deleteQuestion = baseUrl + "/api/WebArd/DeleteQuestion";
    public static String memberData = baseUrl + "/api/WebArd/MemberData";


}
