package rahulb.pdftools.cmd;

import rahulb.pdftools.core.*;

enum Command {
  EncryptPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new EncryptPdfHandler(new EncryptPdfService());
    }
  },

  DecryptPdfs {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new DecryptPdfsHandler(new DecryptPdfsService());
    }
  },

  PdfToImage {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PdfToImageHandler(new PdfToImageService());
    }
  },

  RemovePages {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new RemovePagesHandler(new RemovePagesService());
    }
  },

  AddWatermark {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new AddWatermarkHandler(new AddWatermarkService());
    }
  },

  ConvertToGrayscale {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ConvertToGrayscaleHandler(new ConvertToGrayscaleService());
    }
  },

  ImagesToPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ImagesToPdfHandler(new ImagesToPdfService());
    }
  },

  Pipeline {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PipelineHandler();
    }
  };

  abstract AbstractCommandHandler obtainCommandHandler();
}
