package com.khaipv.attendance.luxand

object FSDK {
    // Error codes
    const val FSDKE_OK = 0
    const val FSDKE_FAILED = -1
    const val FSDKE_NOT_ACTIVATED = -2
    const val FSDKE_OUT_OF_MEMORY = -3
    const val FSDKE_INVALID_ARGUMENT = -4
    const val FSDKE_IO_ERROR = -5
    const val FSDKE_IMAGE_TOO_SMALL = -6
    const val FSDKE_FACE_NOT_FOUND = -7
    const val FSDKE_INSUFFICIENT_BUFFER_SIZE = -8
    const val FSDKE_UNSUPPORTED_IMAGE_EXTENSION = -9
    const val FSDKE_CANNOT_OPEN_FILE = -10
    const val FSDKE_CANNOT_CREATE_FILE = -11
    const val FSDKE_BAD_FILE_FORMAT = -12
    const val FSDKE_FILE_NOT_FOUND = -13
    const val FSDKE_CONNECTION_CLOSED = -14
    const val FSDKE_CONNECTION_FAILED = -15
    const val FSDKE_IP_INIT_FAILED = -16
    const val FSDKE_NEED_SERVER_ACTIVATION = -17
    const val FSDKE_ID_NOT_FOUND = -18
    const val FSDKE_ATTRIBUTE_NOT_DETECTED = -19
    const val FSDKE_INSUFFICIENT_TRACKER_MEMORY_LIMIT = -20
    const val FSDKE_UNKNOWN_ATTRIBUTE = -21
    const val FSDKE_UNSUPPORTED_FILE_VERSION = -22
    const val FSDKE_SYNTAX_ERROR = -23
    const val FSDKE_PARAMETER_NOT_FOUND = -24
    const val FSDKE_INVALID_TEMPLATE = -25
    const val FSDKE_UNSUPPORTED_TEMPLATE_VERSION = -26
    const val FSDKE_CAMERA_INDEX_DOES_NOT_EXIST = -27
    const val FSDKE_PLATFORM_NOT_LICENSED = -28

    // Facial feature count
    const val FSDK_FACIAL_FEATURE_COUNT = 70

    // Facial features
    const val FSDKP_LEFT_EYE = 0
    const val FSDKP_RIGHT_EYE = 1
    const val FSDKP_LEFT_EYE_INNER_CORNER = 24
    const val FSDKP_LEFT_EYE_OUTER_CORNER = 23
    const val FSDKP_LEFT_EYE_LOWER_LINE1 = 38
    const val FSDKP_LEFT_EYE_LOWER_LINE2 = 27
    const val FSDKP_LEFT_EYE_LOWER_LINE3 = 37
    const val FSDKP_LEFT_EYE_UPPER_LINE1 = 35
    const val FSDKP_LEFT_EYE_UPPER_LINE2 = 28
    const val FSDKP_LEFT_EYE_UPPER_LINE3 = 36
    const val FSDKP_LEFT_EYE_LEFT_IRIS_CORNER = 29
    const val FSDKP_LEFT_EYE_RIGHT_IRIS_CORNER = 30
    const val FSDKP_RIGHT_EYE_INNER_CORNER = 25
    const val FSDKP_RIGHT_EYE_OUTER_CORNER = 26
    const val FSDKP_RIGHT_EYE_LOWER_LINE1 = 41
    const val FSDKP_RIGHT_EYE_LOWER_LINE2 = 31
    const val FSDKP_RIGHT_EYE_LOWER_LINE3 = 42
    const val FSDKP_RIGHT_EYE_UPPER_LINE1 = 40
    const val FSDKP_RIGHT_EYE_UPPER_LINE2 = 32
    const val FSDKP_RIGHT_EYE_UPPER_LINE3 = 39
    const val FSDKP_RIGHT_EYE_LEFT_IRIS_CORNER = 33
    const val FSDKP_RIGHT_EYE_RIGHT_IRIS_CORNER = 34
    const val FSDKP_LEFT_EYEBROW_INNER_CORNER = 13
    const val FSDKP_LEFT_EYEBROW_MIDDLE = 16
    const val FSDKP_LEFT_EYEBROW_MIDDLE_LEFT = 18
    const val FSDKP_LEFT_EYEBROW_MIDDLE_RIGHT = 19
    const val FSDKP_LEFT_EYEBROW_OUTER_CORNER = 12
    const val FSDKP_RIGHT_EYEBROW_INNER_CORNER = 14
    const val FSDKP_RIGHT_EYEBROW_MIDDLE = 17
    const val FSDKP_RIGHT_EYEBROW_MIDDLE_LEFT = 20
    const val FSDKP_RIGHT_EYEBROW_MIDDLE_RIGHT = 21
    const val FSDKP_RIGHT_EYEBROW_OUTER_CORNER = 15
    const val FSDKP_NOSE_TIP = 2
    const val FSDKP_NOSE_BOTTOM = 49
    const val FSDKP_NOSE_BRIDGE = 22
    const val FSDKP_NOSE_LEFT_WING = 43
    const val FSDKP_NOSE_LEFT_WING_OUTER = 45
    const val FSDKP_NOSE_LEFT_WING_LOWER = 47
    const val FSDKP_NOSE_RIGHT_WING = 44
    const val FSDKP_NOSE_RIGHT_WING_OUTER = 46
    const val FSDKP_NOSE_RIGHT_WING_LOWER = 48
    const val FSDKP_MOUTH_RIGHT_CORNER = 3
    const val FSDKP_MOUTH_LEFT_CORNER = 4
    const val FSDKP_MOUTH_TOP = 54
    const val FSDKP_MOUTH_TOP_INNER = 61
    const val FSDKP_MOUTH_BOTTOM = 55
    const val FSDKP_MOUTH_BOTTOM_INNER = 64
    const val FSDKP_MOUTH_LEFT_TOP = 56
    const val FSDKP_MOUTH_LEFT_TOP_INNER = 60
    const val FSDKP_MOUTH_RIGHT_TOP = 57
    const val FSDKP_MOUTH_RIGHT_TOP_INNER = 62
    const val FSDKP_MOUTH_LEFT_BOTTOM = 58
    const val FSDKP_MOUTH_LEFT_BOTTOM_INNER = 63
    const val FSDKP_MOUTH_RIGHT_BOTTOM = 59
    const val FSDKP_MOUTH_RIGHT_BOTTOM_INNER = 65
    const val FSDKP_NASOLABIAL_FOLD_LEFT_UPPER = 50
    const val FSDKP_NASOLABIAL_FOLD_LEFT_LOWER = 52
    const val FSDKP_NASOLABIAL_FOLD_RIGHT_UPPER = 51
    const val FSDKP_NASOLABIAL_FOLD_RIGHT_LOWER = 53
    const val FSDKP_CHIN_BOTTOM = 11
    const val FSDKP_CHIN_LEFT = 9
    const val FSDKP_CHIN_RIGHT = 10
    const val FSDKP_FACE_CONTOUR1 = 7
    const val FSDKP_FACE_CONTOUR2 = 5
    const val FSDKP_FACE_CONTOUR12 = 6
    const val FSDKP_FACE_CONTOUR13 = 8
    const val FSDKP_FACE_CONTOUR14 = 66
    const val FSDKP_FACE_CONTOUR15 = 67
    const val FSDKP_FACE_CONTOUR16 = 68
    const val FSDKP_FACE_CONTOUR17 = 69
    external fun ActivateLibrary(LicenseKey: String?): Int

    //public static native int GetHardware_ID(String HardwareID[]); //not implemented
    external fun GetLicenseInfo(LicenseInfo: Array<String?>?): Int
    external fun SetNumThreads(Num: Int): Int
    external fun GetNumThreads(Num: IntArray?): Int
    external fun Initialize(): Int
    external fun Finalize(): Int
    external fun CreateEmptyImage(Image: HImage?): Int
    external fun FreeImage(Image: HImage?): Int
    external fun LoadImageFromFile(Image: HImage?, FileName: String?): Int
    external fun LoadImageFromFileWithAlpha(Image: HImage?, FileName: String?): Int
    external fun SaveImageToFile(Image: HImage?, FileName: String?): Int
    external fun SetJpegCompressionQuality(Quality: Int): Int
    external fun GetImageWidth(Image: HImage?, Width: IntArray?): Int
    external fun GetImageHeight(Image: HImage?, Height: IntArray?): Int
    external fun LoadImageFromBuffer(
        Image: HImage?,
        Buffer: ByteArray?,
        Width: Int,
        Height: Int,
        ScanLine: Int,
        ImageMode: FSDK_IMAGEMODE?
    ): Int

    external fun GetImageBufferSize(
        Image: HImage?,
        BufSize: IntArray?,
        ImageMode: FSDK_IMAGEMODE?
    ): Int

    external fun SaveImageToBuffer(
        Image: HImage?,
        Buffer: ByteArray?,
        ImageMode: FSDK_IMAGEMODE?
    ): Int

    external fun LoadImageFromJpegBuffer(Image: HImage?, Buffer: ByteArray?, BufferLength: Int): Int
    external fun LoadImageFromPngBuffer(Image: HImage?, Buffer: ByteArray?, BufferLength: Int): Int
    external fun LoadImageFromPngBufferWithAlpha(
        Image: HImage?,
        Buffer: ByteArray?,
        BufferLength: Int
    ): Int

    external fun DetectFace(Image: HImage?, FacePosition: TFacePosition?): Int
    external fun DetectMultipleFaces(Image: HImage?, FacePositions: TFaces?): Int
    external fun SetFaceDetectionParameters(
        HandleArbitraryRotations: Boolean,
        DetermineFaceRotationAngle: Boolean,
        InternalResizeWidth: Int
    ): Int

    external fun SetFaceDetectionThreshold(Threshold: Int): Int
    external fun GetDetectedFaceConfidence(Confidence: IntArray?): Int
    external fun DetectFacialFeatures(Image: HImage?, FacialFeatures: FSDK_Features?): Int
    external fun DetectFacialFeaturesInRegion(
        Image: HImage?,
        FacePosition: TFacePosition?,
        FacialFeatures: FSDK_Features?
    ): Int

    external fun DetectEyes(Image: HImage?, Eyes: FSDK_Features?): Int
    external fun DetectEyesInRegion(
        Image: HImage?,
        FacePosition: TFacePosition?,
        Eyes: FSDK_Features?
    ): Int

    external fun CopyImage(SourceImage: HImage?, DestImage: HImage?): Int
    external fun ResizeImage(SourceImage: HImage?, ratio: Double, DestImage: HImage?): Int
    external fun RotateImage90(SourceImage: HImage?, Multiplier: Int, DestImage: HImage?): Int
    external fun RotateImage(SourceImage: HImage?, angle: Double, DestImage: HImage?): Int
    external fun RotateImageCenter(
        SourceImage: HImage?,
        angle: Double,
        xCenter: Double,
        yCenter: Double,
        DestImage: HImage?
    ): Int

    external fun CopyRect(
        SourceImage: HImage?,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        DestImage: HImage?
    ): Int

    external fun CopyRectReplicateBorder(
        SourceImage: HImage?,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        DestImage: HImage?
    ): Int

    external fun MirrorImage(Image: HImage?, UseVerticalMirroringInsteadOfHorizontal: Boolean): Int
    external fun ExtractFaceImage(
        Image: HImage?,
        FacialFeatures: FSDK_Features?,
        Width: Int,
        Height: Int,
        ExtractedFaceImage: HImage?,
        ResizedFeatures: FSDK_Features?
    ): Int

    external fun GetFaceTemplate(Image: HImage?, FaceTemplate: FSDK_FaceTemplate?): Int
    external fun GetFaceTemplateInRegion(
        Image: HImage?,
        FacePosition: TFacePosition?,
        FaceTemplate: FSDK_FaceTemplate?
    ): Int

    external fun GetFaceTemplateUsingFeatures(
        Image: HImage?,
        FacialFeatures: FSDK_Features?,
        FaceTemplate: FSDK_FaceTemplate?
    ): Int

    external fun GetFaceTemplateUsingEyes(
        Image: HImage?,
        EyeCoords: FSDK_Features?,
        FaceTemplate: FSDK_FaceTemplate?
    ): Int

    external fun MatchFaces(
        FaceTemplate1: FSDK_FaceTemplate?,
        FaceTemplate2: FSDK_FaceTemplate?,
        Similarity: FloatArray?
    ): Int

    external fun GetMatchingThresholdAtFAR(FARValue: Float, Threshold: FloatArray?): Int
    external fun GetMatchingThresholdAtFRR(FRRValue: Float, Threshold: FloatArray?): Int
    external fun CreateTracker(Tracker: HTracker?): Int
    external fun FreeTracker(Tracker: HTracker?): Int
    external fun ClearTracker(Tracker: HTracker?): Int
    external fun SetTrackerParameter(
        Tracker: HTracker?,
        ParameterName: String?,
        ParameterValue: String?
    ): Int

    external fun SetTrackerMultipleParameters(
        Tracker: HTracker?,
        Parameters: String?,
        ErrorPosition: IntArray?
    ): Int

    external fun GetTrackerParameter(
        Tracker: HTracker?,
        ParameterName: String?,
        ParameterValue: Array<String?>?,
        MaxSizeInBytes: Int
    ): Int

    external fun FeedFrame(
        Tracker: HTracker?,
        CameraIdx: Long,
        Image: HImage?,
        FaceCount: LongArray?,
        IDs: LongArray?
    ): Int

    external fun GetTrackerEyes(
        Tracker: HTracker?,
        CameraIdx: Long,
        ID: Long,
        Eyes: FSDK_Features?
    ): Int

    external fun GetTrackerFacialFeatures(
        Tracker: HTracker?,
        CameraIdx: Long,
        ID: Long,
        FacialFeatures: FSDK_Features?
    ): Int

    external fun GetTrackerFacePosition(
        Tracker: HTracker?,
        CameraIdx: Long,
        ID: Long,
        FacePosition: TFacePosition?
    ): Int

    external fun LockID(Tracker: HTracker?, ID: Long): Int
    external fun UnlockID(Tracker: HTracker?, ID: Long): Int
    external fun PurgeID(Tracker: HTracker?, ID: Long): Int
    external fun SetName(Tracker: HTracker?, ID: Long, Name: String?): Int
    external fun GetName(
        Tracker: HTracker?,
        ID: Long,
        Name: Array<String?>?,
        MaxSizeInBytes: Long
    ): Int

    external fun GetAllNames(
        Tracker: HTracker?,
        ID: Long,
        Names: Array<String?>?,
        MaxSizeInBytes: Long
    ): Int

    external fun GetIDReassignment(Tracker: HTracker?, ID: Long, ReassignedID: LongArray?): Int
    external fun GetSimilarIDCount(Tracker: HTracker?, ID: Long, Count: LongArray?): Int
    external fun GetSimilarIDList(Tracker: HTracker?, ID: Long, SimilarIDList: LongArray?): Int
    external fun SaveTrackerMemoryToFile(Tracker: HTracker?, FileName: String?): Int
    external fun LoadTrackerMemoryFromFile(Tracker: HTracker?, FileName: String?): Int
    external fun GetTrackerMemoryBufferSize(Tracker: HTracker?, BufSize: LongArray?): Int
    external fun SaveTrackerMemoryToBuffer(Tracker: HTracker?, Buffer: ByteArray?): Int
    external fun LoadTrackerMemoryFromBuffer(Tracker: HTracker?, Buffer: ByteArray?): Int
    external fun GetTrackerFacialAttribute(
        Tracker: HTracker?,
        CameraIdx: Long,
        ID: Long,
        AttributeName: String?,
        AttributeValues: Array<String?>?,
        MaxSizeInBytes: Long
    ): Int

    external fun DetectFacialAttributeUsingFeatures(
        Image: HImage?,
        FacialFeatures: FSDK_Features?,
        AttributeName: String?,
        AttributeValues: Array<String?>?,
        MaxSizeInBytes: Long
    ): Int

    external fun GetValueConfidence(
        AttributeValues: String?,
        Value: String?,
        Confidence: FloatArray?
    ): Int

    external fun SetHTTPProxy(
        ServerNameOrIPAddress: String?,
        Port: Short,
        UserName: String?,
        Password: String?
    ): Int

    external fun OpenIPVideoCamera(
        CompressionType: FSDK_VIDEOCOMPRESSIONTYPE?,
        URL: String?,
        Username: String?,
        Password: String?,
        TimeoutSeconds: Int,
        CameraHandle: HCamera?
    ): Int

    external fun CloseVideoCamera(CameraHandle: HCamera?): Int
    external fun GrabFrame(CameraHandle: HCamera?, Image: HImage?): Int
    external fun InitializeCapturing(): Int
    external fun FinalizeCapturing(): Int

    // Types
    class FSDK_VIDEOCOMPRESSIONTYPE {
        var type = 0

        companion object {
            const val FSDK_MJPEG = 0
        }
    }

    class FSDK_IMAGEMODE {
        var mode = 0

        companion object {
            const val FSDK_IMAGE_GRAYSCALE_8BIT = 0
            const val FSDK_IMAGE_COLOR_24BIT = 1
            const val FSDK_IMAGE_COLOR_32BIT = 2
        }
    }

    class HImage {
        //to pass himage "by reference"
        protected var himage = 0
    }

    class HCamera {
        //to pass hcamera "by reference"
        protected var hcamera = 0
    }

    class HTracker {
        protected var htracker = 0
    }

    class TFacePosition {
        var xc = 0
        var yc = 0
        var w = 0
        var padding = 0
        var angle = 0.0
    }

    class TFaces {
        var faces: Array<TFacePosition>?
        var maxFaces: Int

        constructor() {
            maxFaces = 100
            faces = null
        }

        constructor(MaxFaces: Int) {
            maxFaces = MaxFaces
            faces = null
        }
    }

    class TPoint {
        var x = 0
        var y = 0
    }

    class FSDK_Features {
        var features = arrayOfNulls<TPoint>(FSDK_FACIAL_FEATURE_COUNT)
    }

    class FSDK_FaceTemplate {
        var template = ByteArray(1040)
    }

    init {
        //System.loadLibrary("stlport_shared");
        System.loadLibrary("fsdk")
    }
}
