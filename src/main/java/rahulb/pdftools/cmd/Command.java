package rahulb.pdftools.cmd;

import rahulb.pdftools.core.AddWatermarkService;

enum Command {
  EncryptPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new EncryptPdfHandler();
    }
  },

  DecryptPdfs {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new DecryptPdfsHandler();
    }
  },

  PdfToImage {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PdfToImageHandler();
    }
  },

  RemovePages {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new RemovePagesHandler();
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
      return new ConvertToGrayscaleHandler();
    }
  },

  ImagesToPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ImagesToPdfHandler();
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
