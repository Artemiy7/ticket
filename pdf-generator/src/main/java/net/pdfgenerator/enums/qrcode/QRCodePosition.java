package net.pdfgenerator.enums.qrcode;

public enum QRCodePosition {
        TOP_LEFT(0f, 700f),
        TOP_RIGHT(500f, 700f),
        BOTTOM_LEFT(0f, 0f),
        BOTTOM_RIGHT(500f, 0f),
        LEFT_CENTER(20f, 477f);

        private final float absoluteX;
        private final float absoluteY;

        QRCodePosition(float absoluteX, float absoluteY) {
            this.absoluteX = absoluteX;
            this.absoluteY = absoluteY;
        }

        public float getAbsoluteX() {
            return absoluteX;
        }

        public float getAbsoluteY() {
            return absoluteY;
        }
}
